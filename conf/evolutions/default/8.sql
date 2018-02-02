# --- !Ups
alter table usuario add column admin boolean;

# --- !Downs
alter table usuario drop column admin;