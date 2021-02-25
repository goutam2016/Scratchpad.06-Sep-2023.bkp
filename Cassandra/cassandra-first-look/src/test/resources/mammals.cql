-- Export of keyspace mammals
CREATE KEYSPACE mammals
WITH durable_writes = true
AND replication = {
	'class' : 'SimpleStrategy',
	'replication_factor' : 3
};

CREATE TYPE mammals.genus (
	name text,
	description text
);

CREATE TABLE mammals.primate (
	species text,
	arboreal boolean,
	avgweight double,
	genus FROZEN<genus>,
	places SET<text>,
	PRIMARY KEY (species)
) WITH bloom_filter_fp_chance = 0.01
AND comment = ''
AND crc_check_chance = 1.0
AND dclocal_read_repair_chance = 0.1
AND default_time_to_live = 0
AND gc_grace_seconds = 864000
AND max_index_interval = 2048
AND memtable_flush_period_in_ms = 0
AND min_index_interval = 128
AND read_repair_chance = 0.0
AND speculative_retry = '99.0PERCENTILE'
AND caching = {
	'keys' : 'ALL',
	'rows_per_partition' : 'NONE'
}
AND compression = {
	'chunk_length_in_kb' : 64,
	'class' : 'LZ4Compressor',
	'enabled' : true
}
AND compaction = {
	'class' : 'SizeTieredCompactionStrategy',
	'max_threshold' : 32,
	'min_threshold' : 4
};

