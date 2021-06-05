import {BaseEntity, Column, Entity, JoinTable, ManyToMany, PrimaryGeneratedColumn, Unique} from "typeorm";

@Entity()
@Unique('owner_name_unique', ['owner', 'name'])
export class GithubRepository extends BaseEntity {
    @PrimaryGeneratedColumn()
    id: number;
    @Column()
    owner: string;
    @Column()
    name: string;
    @Column()
    stars: number;
    @Column()
    forks: number;
    @ManyToMany(() => GithubRepository)
    @JoinTable()
    dependencies: GithubRepository[]
}