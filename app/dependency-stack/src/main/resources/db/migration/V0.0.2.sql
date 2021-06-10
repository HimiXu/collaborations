CREATE TABLE github_repository (
                                   id SERIAL,
                                   name TEXT,
                                   owner TEXT,
                                   stars INTEGER
);

CREATE TABLE github_repository_dependency (
                                              id SERIAL,
                                              "from" INTEGER,
                                              "to" INTEGER
);

CREATE FUNCTION delete_dependencies_function() RETURNS TRIGGER AS $_$
BEGIN
    DELETE FROM github_repository_dependency WHERE "from" = OLD.id OR "to" = OLD.id;
    RETURN OLD;
END $_$ LANGUAGE 'plpgsql';

CREATE TRIGGER delete_dependencies_trigger
    AFTER DELETE ON github_repository
    FOR EACH ROW EXECUTE PROCEDURE delete_dependencies_function()



