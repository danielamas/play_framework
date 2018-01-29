# --- !Ups

alter table usuario add column verificado boolean;


# --- !Downs

alter table usuario drop column verificado;