import {Connection, createConnection} from "typeorm";
import {GithubRepository} from "../domain/repository";

export const connectPostgres = async (): Promise<Connection> => {
    return createConnection({
        type: "postgres",
        host: process.env.POSTGRES_HOST,
        port: Number(process.env.POSTGRES_PORT),
        username: process.env.POSTGRES_USER,
        password: process.env.POSTGRES_PASSWORD,
        database: process.env.POSTGRES_DB,
        entities: [
            GithubRepository
        ],
        synchronize: true,
        logging: false
    })
}
