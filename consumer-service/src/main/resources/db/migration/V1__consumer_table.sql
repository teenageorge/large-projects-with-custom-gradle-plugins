create table consumer
(
  id         INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  email      VARCHAR                 NOT NULL,
  first_name VARCHAR                 NOT NULL,
  last_name  VARCHAR                 NOT NULL,
  created_at TIMESTAMP DEFAULT now() NOT NULL
);

create unique index consumer_email_uindex
  on consumer (email);