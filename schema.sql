-- ============================

-- This file was created using Derby's dblook utility.
-- Timestamp: 2020-12-26 10:43:16.644
-- Source database is: folklore/data/database
-- Connection URL is: jdbc:derby:folklore/data/database
-- appendLogs: false

-- ----------------------------------------------
-- DDL Statements for schemas
-- ----------------------------------------------

CREATE SCHEMA "ADMIN";

-- ----------------------------------------------
-- DDL Statements for tables
-- ----------------------------------------------

CREATE TABLE "ADMIN"."INSTRUMENT" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "NAME" VARCHAR(255));

CREATE TABLE "ADMIN"."ETHNOGRAPHIC_REGION" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "NAME" VARCHAR(255));

CREATE TABLE "ADMIN"."SOURCE_TYPE" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "NAME" VARCHAR(255));

CREATE TABLE "ADMIN"."ARTIST" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "NAME" VARCHAR(255), "NOTE" VARCHAR(255), "INSTRUMENT_ID" INTEGER);

CREATE TABLE "ADMIN"."SOURCE" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "SIGNATURE" VARCHAR(255), "TYPE_ID" INTEGER);

CREATE TABLE "ADMIN"."ARTIST_MISSIONS" ("ARTIST_ID" INTEGER NOT NULL, "MISSIONS" VARCHAR(255));

CREATE TABLE "ADMIN"."FOLKLORE_TRACK" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "DURATION" VARCHAR(255), "NOTE" VARCHAR(255), "TITLE" VARCHAR(255), "ACCOMPANIMENTPERFORMER_ID" INTEGER, "ARRANGEMENTAUTHOR_ID" INTEGER, "AUTHOR_ID" INTEGER, "CONDUCTOR_ID" INTEGER, "PERFORMER_ID" INTEGER, "SOLOIST_ID" INTEGER, "SOURCE_ID" INTEGER, "ETHNOGRAPHICREGION_ID" INTEGER);

-- ----------------------------------------------
-- DDL Statements for keys
-- ----------------------------------------------

-- PRIMARY/UNIQUE
ALTER TABLE "ADMIN"."ETHNOGRAPHIC_REGION" ADD CONSTRAINT "SQL180325094622350" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."INSTRUMENT" ADD CONSTRAINT "SQL180325094622910" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."FOLKLORE_TRACK" ADD CONSTRAINT "SQL180325094622690" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."SOURCE_TYPE" ADD CONSTRAINT "SQL180325094623540" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."SOURCE" ADD CONSTRAINT "SQL180325094623340" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."ARTIST" ADD CONSTRAINT "SQL180325094622050" PRIMARY KEY ("ID");

-- FOREIGN
ALTER TABLE "ADMIN"."FOLKLORE_TRACK" ADD CONSTRAINT "FKCXUCLJ3CMO11028JHLX4US7GF" FOREIGN KEY ("ACCOMPANIMENTPERFORMER_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_TRACK" ADD CONSTRAINT "FKKOBKE89TNGCECNGYTXHUMCG68" FOREIGN KEY ("ARRANGEMENTAUTHOR_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_TRACK" ADD CONSTRAINT "FKLFENQEYS63JY9IQ6DFXWYTBT9" FOREIGN KEY ("AUTHOR_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_TRACK" ADD CONSTRAINT "FKMMGCYUJR5NWNLYCCM735G6MM7" FOREIGN KEY ("CONDUCTOR_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_TRACK" ADD CONSTRAINT "FKOAFMX4G9F8FK1RFL1FNSUNWTI" FOREIGN KEY ("PERFORMER_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_TRACK" ADD CONSTRAINT "FKACBJK6I8JE4BL7MHCFWFC2JYJ" FOREIGN KEY ("SOLOIST_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_TRACK" ADD CONSTRAINT "FKRS14QOSKMLGH8898YR6G6L8YJ" FOREIGN KEY ("SOURCE_ID") REFERENCES "ADMIN"."SOURCE" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_TRACK" ADD CONSTRAINT "FKDY7S5LL5TV89BGCKX7A3MCJDV" FOREIGN KEY ("ETHNOGRAPHICREGION_ID") REFERENCES "ADMIN"."ETHNOGRAPHIC_REGION" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."SOURCE" ADD CONSTRAINT "FKBAIHHJXQN8O5E2PLXWEAQKVNY" FOREIGN KEY ("TYPE_ID") REFERENCES "ADMIN"."SOURCE_TYPE" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."ARTIST" ADD CONSTRAINT "FKCKMMQ5G05AVW7O4MCTS12058B" FOREIGN KEY ("INSTRUMENT_ID") REFERENCES "ADMIN"."INSTRUMENT" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."ARTIST_MISSIONS" ADD CONSTRAINT "FK4G17VF8TVN3LGMD3F2YVWQ0KR" FOREIGN KEY ("ARTIST_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

