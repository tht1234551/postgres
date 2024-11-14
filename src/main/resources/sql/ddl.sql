-- DROP TABLE IF EXISTS public.board CASCADE;
DROP TABLE IF EXISTS public.board;

CREATE TABLE public.board
(
    no        bigint             not null primary key,
    title     varchar(300)       not null,
    content   varchar(3000)      not null,
    writer    varchar(300)       not null,
    create_at date default now() not null,
    update_at date default now() not null
);

ALTER TABLE public.board OWNER TO admin;

