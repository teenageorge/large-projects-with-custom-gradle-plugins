create table orders
(
  id          INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  consumer_id INTEGER                 NOT NULL,
  price       NUMERIC                 NOT NULL,
  created_at  TIMESTAMP DEFAULT now() NOT NULL
);
