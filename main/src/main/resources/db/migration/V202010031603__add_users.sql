CREATE TABLE public.users (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR NOT NULL,
    patronymic VARCHAR NOT NULL,
    lastname VARCHAR NOT NULL,
    telegram_id int,
    about VARCHAR NOT NULL
);

CREATE TABLE public.relations (
    from_id int REFERENCES public.users,
    to_id int REFERENCES public.users,
    relation_type VARCHAR NOT NULL
);