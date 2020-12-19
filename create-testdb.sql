connect 'jdbc:derby:testdb;create=true;';
run 'schema.sql';
run 'seed.sql';
disconnect;
exit;
