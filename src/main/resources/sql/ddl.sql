-- 시퀀스 생성
DROP SEQUENCE IF EXISTS BOARD_NO_SEQ CASCADE;
CREATE SEQUENCE BOARD_NO_SEQ START 1 INCREMENT 1;

-- 테이블 생성
-- DROP TABLE IF EXISTS public.board CASCADE;
DROP TABLE IF EXISTS public.board;
CREATE TABLE public.board
(
    no         BIGINT    DEFAULT nextval('BOARD_NO_SEQ'::regclass) PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    writer     VARCHAR(300) not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE public.board
    OWNER TO admin;

