import express, {Request, Response} from "express";
import {gql, GraphQLClient} from "graphql-request";
import {GithubRepository} from "../domain/github-repository";
import {getConnection, getManager, getRepository} from "typeorm";

export const router = express.Router()

const query = gql`
query getDependencies($owner: String!, $name: String!){
    repository(owner: $owner, name: $name){
        stargazerCount
        forkCount
        dependencyGraphManifests {
            nodes {
                dependencies {
                    nodes {
                        repository {
                            nameWithOwner
                            stargazerCount
                            forkCount
                        }
                    }
                }
            }
        }
    }
}
`
const headers = {
    authorization: 'Bearer '+process.env.GITHUB_AUTH_TOKEN,
    accept: "application/vnd.github.hawkgirl-preview+json"
}
const client = new GraphQLClient(String(process.env.GITHUB_GRAPHQL_API),{
    credentials: 'include',
    mode: 'cors',
    headers
})
const analyze = async (req: Request, res: Response) => {
    const owner = req.body.owner;
    const name = req.body.name;
    const variables = { owner, name }
    const data = await client.request(query, variables, headers)
    const repo = new GithubRepository();
    const repoData = data.repository;
    repo.name = name;
    repo.owner = owner;
    repo.forks = repoData.forkCount;
    repo.stars = repoData.stargazerCount;
    const dependencies: {[name: string]: GithubRepository} = {}
    await repoData.dependencyGraphManifests.nodes.forEach(
        async (node: {dependencies: any}) => {
           node.dependencies.nodes.filter((n: {repository: any}) => n != null && n.repository != null).forEach(async (node2: {repository: {nameWithOwner: string, stargazerCount: number, forkCount:number}})=> {
               const dep = new GithubRepository();
               dep.owner = node2.repository.nameWithOwner.split('/')[0]
               dep.name = node2.repository.nameWithOwner.split('/')[1]
               dep.stars = node2.repository.stargazerCount
               dep.forks = node2.repository.forkCount
               dependencies[dep.owner+'.'+dep.name] = dep
           })
        }
    )
    repo.dependencies = Object.values(dependencies);
    await getRepository(GithubRepository).createQueryBuilder()
        .insert()
        .values(repo.dependencies)
        .onConflict(`("owner","name") DO NOTHING`)
        .execute()
    const existing = await getRepository(GithubRepository).createQueryBuilder('gr')
        .select('gr.id')
        .where('gr.owner = :owner AND gr.name = :name', {owner: repo.owner, name: repo.name})
        .getOne()
    if (existing != undefined) repo.id = existing.id
    await getRepository(GithubRepository).save(repo);
    res.status(200).json(repo.id)
}

router.post('/analyze', analyze)