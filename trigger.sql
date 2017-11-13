DROP EVENT TRIGGER etg_notify_create_table;
DROP FUNCTION fn_notify_create_table();

-----

CREATE OR REPLACE FUNCTION fn_notify_create_table()
  RETURNS event_trigger AS
$func$

DECLARE r RECORD;

BEGIN
FOR r IN SELECT * FROM pg_event_trigger_ddl_commands() LOOP
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

CREATE TABLE table99 (
	id bigint PRIMARY KEY,
	insert_timestamp timestamp,
	value char varying(10),
	num numeric
);

SELECT * FROM information_schema.columns
WHERE 	  table_schema = 'public'
AND 	  table_name = 'table99';

SELECT 	  column_name
	, data_type 
	, (CASE 
		WHEN data_type = 'bigint' THEN numeric_precision
		WHEN data_type = 'timestamp without time zone' THEN datetime_precision
		WHEN data_type = 'numeric' THEN numeric_precision_radix
		ELSE character_maximum_length
	  END) AS data_length
	, CASE WHEN column_name = (		
		SELECT column_name 
		FROM information_schema.key_column_usage kc 
		WHERE table_name = 'table99'
		) THEN 1 ELSE 0 END AS is_primary_key
FROM 	  information_schema.columns
WHERE 	  table_schema = 'public'
AND 	  table_name = 'table99'
;