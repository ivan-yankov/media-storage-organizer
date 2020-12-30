connect 'jdbc:derby:test-data/database;create=true;';
run 'schema.sql';
run 'seed.sql';
disconnect;
exit;
