# Vault Test Service

Nov 12th 2019

###### Prerequisites

Ensure that a copy of the "vault" CLI executable is available in the same folder as the Makefile

###### Usage

To start the stack (Vault Server & MySQL) and setup specific properties in Vault execute:

```
make start-all
```

When running, try the following commands:

```
make read-config

make read-actors

make write-actor

make refresh
```

The times for theTTL of the DB connection are in the Makefile (currently set to 3m), whilst the refresh of the DB credentials is configured via the CRON property in the bootstrap.yml file (currently every 2m)

**NOTE:** The refresh time should be shorter than the TTL time.

To stop the stack execute:

```
make down
```

Useful supporting code taken from here:

https://www.epundit.co.uk/blogs/default/2017/09/07/1504773780000.html

