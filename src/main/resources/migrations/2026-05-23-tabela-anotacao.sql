-- ============================================================
-- Migration: tabela T_ANOTACAO + SEQ_ANOTACAO
-- Data: 2026-05-23
-- ============================================================
-- Persiste anotações que antes viviam só no localStorage do app.
--
-- Regras:
--   - autor_tipo ∈ {dentista, colaborador}
--   - sobre_tipo ∈ {dentista, paciente}
--   - sem FK em autor_id/sobre_id pois apontam para tabelas diferentes
--     dependendo do tipo (polimorfismo simples no app)
-- ============================================================

CREATE TABLE T_ANOTACAO (
    ID_ANOTACAO  NUMBER          NOT NULL,
    TEXTO        VARCHAR2(500)   NOT NULL,
    DATA         DATE            NOT NULL,
    AUTOR_TIPO   VARCHAR2(15)    NOT NULL,
    AUTOR_ID     NUMBER          NOT NULL,
    SOBRE_TIPO   VARCHAR2(15)    NOT NULL,
    SOBRE_ID     NUMBER          NOT NULL,
    CONSTRAINT T_ANOTACAO_PK     PRIMARY KEY (ID_ANOTACAO),
    CONSTRAINT CK_ANOT_AUTOR_TIPO CHECK (AUTOR_TIPO IN ('dentista','colaborador')),
    CONSTRAINT CK_ANOT_SOBRE_TIPO CHECK (SOBRE_TIPO IN ('dentista','paciente'))
);

CREATE SEQUENCE SEQ_ANOTACAO START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

COMMIT;
