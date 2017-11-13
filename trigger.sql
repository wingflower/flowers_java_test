DROP EVENT TRIGGER etg_notify_create_table;
DROP FUNCTION fn_notify_create_table();

-----

CREATE OR REPLACE FUNCTION fn_notify_create_table()
  RETURNS event_trigger AS
$func$

DECLARE r RECORD;

BEGIN
FOR r IN SELECT * FROM pg_event_trigger_ddl_commands() LOOP
        RAISE NOTICE 'caught % event on %', r.command_tag, r.object_identity;
        PERFORM pg_notify('mymessage', r.object_identity);
    END LOOP;

END
$func$ LANGUAGE plpgsql;
-----

-----  
CREATE EVENT TRIGGER etg_notify_create_table ON ddl_command_end
   WHEN tag IN ('CREATE TABLE')
   EXECUTE PROCEDURE fn_notify_create_table();
-----
