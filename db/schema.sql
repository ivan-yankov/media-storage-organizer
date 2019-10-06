-- ============================

-- This file was created using Derby's dblook utility.
-- Timestamp: 2019-10-06 10:15:33.511
-- Source database is: database/mso
-- Connection URL is: jdbc:derby:database/mso
-- Specified schema is: admin
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

CREATE TABLE "ADMIN"."ALBUM" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "COLLECTION_SIGNATURE" VARCHAR(255), "NOTE" VARCHAR(255), "TITLE" VARCHAR(255));

CREATE TABLE "ADMIN"."SOURCE" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "SIGNATURE" VARCHAR(255), "TYPE_ID" INTEGER);

CREATE TABLE "ADMIN"."ARTIST_MISSIONS" ("ARTIST_ID" INTEGER NOT NULL, "MISSIONS" VARCHAR(255));

CREATE TABLE "ADMIN"."FOLKLORE_PIECE" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "ALBUM_TRACK_ORDER" INTEGER, "DURATION" VARCHAR(255), "NOTE" VARCHAR(255), "TITLE" VARCHAR(255), "ACCOMPANIMENTPERFORMER_ID" INTEGER, "ALBUM_ID" INTEGER, "ARRANGEMENTAUTHOR_ID" INTEGER, "AUTHOR_ID" INTEGER, "CONDUCTOR_ID" INTEGER, "PERFORMER_ID" INTEGER, "RECORD_ID" INTEGER, "SOLOIST_ID" INTEGER, "SOURCE_ID" INTEGER, "ETHNOGRAPHICREGION_ID" INTEGER);

CREATE TABLE "ADMIN"."RECORD" ("ID" INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1), "DATA" BLOB(2147483647), "DATA_FORMAT" VARCHAR(255));

-- ----------------------------------------------
-- DDL Statements for keys
-- ----------------------------------------------

-- PRIMARY/UNIQUE
ALTER TABLE "ADMIN"."ALBUM" ADD CONSTRAINT "SQL180325094621860" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."ETHNOGRAPHIC_REGION" ADD CONSTRAINT "SQL180325094622350" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."INSTRUMENT" ADD CONSTRAINT "SQL180325094622910" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "SQL180325094622690" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."SOURCE_TYPE" ADD CONSTRAINT "SQL180325094623540" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."SOURCE" ADD CONSTRAINT "SQL180325094623340" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."ARTIST" ADD CONSTRAINT "SQL180325094622050" PRIMARY KEY ("ID");

ALTER TABLE "ADMIN"."RECORD" ADD CONSTRAINT "SQL180325094623120" PRIMARY KEY ("ID");

-- FOREIGN
ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKCXUCLJ3CMO11028JHLX4US7GF" FOREIGN KEY ("ACCOMPANIMENTPERFORMER_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKHI93PA65JLCNLREF6W52UYU6V" FOREIGN KEY ("ALBUM_ID") REFERENCES "ADMIN"."ALBUM" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKKOBKE89TNGCECNGYTXHUMCG68" FOREIGN KEY ("ARRANGEMENTAUTHOR_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKLFENQEYS63JY9IQ6DFXWYTBT9" FOREIGN KEY ("AUTHOR_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKMMGCYUJR5NWNLYCCM735G6MM7" FOREIGN KEY ("CONDUCTOR_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKOAFMX4G9F8FK1RFL1FNSUNWTI" FOREIGN KEY ("PERFORMER_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKKET91EILNL3DHC6B3X6E4GC15" FOREIGN KEY ("RECORD_ID") REFERENCES "ADMIN"."RECORD" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKACBJK6I8JE4BL7MHCFWFC2JYJ" FOREIGN KEY ("SOLOIST_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKRS14QOSKMLGH8898YR6G6L8YJ" FOREIGN KEY ("SOURCE_ID") REFERENCES "ADMIN"."SOURCE" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."FOLKLORE_PIECE" ADD CONSTRAINT "FKDY7S5LL5TV89BGCKX7A3MCJDV" FOREIGN KEY ("ETHNOGRAPHICREGION_ID") REFERENCES "ADMIN"."ETHNOGRAPHIC_REGION" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."SOURCE" ADD CONSTRAINT "FKBAIHHJXQN8O5E2PLXWEAQKVNY" FOREIGN KEY ("TYPE_ID") REFERENCES "ADMIN"."SOURCE_TYPE" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."ARTIST" ADD CONSTRAINT "FKCKMMQ5G05AVW7O4MCTS12058B" FOREIGN KEY ("INSTRUMENT_ID") REFERENCES "ADMIN"."INSTRUMENT" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "ADMIN"."ARTIST_MISSIONS" ADD CONSTRAINT "FK4G17VF8TVN3LGMD3F2YVWQ0KR" FOREIGN KEY ("ARTIST_ID") REFERENCES "ADMIN"."ARTIST" ("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;

