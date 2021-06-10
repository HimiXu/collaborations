import {config} from "dotenv";
config()
import express from "express";
import {connectPostgres} from "./infrastracture/postgres"
import {router} from "./api/repositories-contoller";
import bodyParser from "body-parser";

const main = async () => {
    const app = express()
    await connectPostgres();
    app.use(bodyParser.json())
    app.use(bodyParser.urlencoded({extended: false}))
    app.use(router)
    app.listen(process.env.SERVER_PORT, () => console.log("server is listening on "+process.env.SERVER_PORT))
}
main();