DROP EVENT TRIGGER notify_create_table;
DROP FUNCTION notify_create_table();

CREATE OR REPLACE FUNCTION notify_create_table()
  RETURNS event_trigger AS
$func$
BEGIN

PERFORM pg_notify('mymessage', 'new table');

END
$func$ LANGUAGE plpgsql;

-----  
CREATE EVENT TRIGGER notify_create_table ON ddl_command_end
   WHEN tag IN ('CREATE TABLE')
   EXECUTE PROCEDURE notify_create_table();
-----
