-- ============================================================
-- Migration: estende sobre_tipo de T_ANOTACAO para incluir 'atendimento'
-- Data: 2026-05-23
-- ============================================================

ALTER TABLE T_ANOTACAO DROP CONSTRAINT CK_ANOT_SOBRE_TIPO;

ALTER TABLE T_ANOTACAO ADD CONSTRAINT CK_ANOT_SOBRE_TIPO
  CHECK (SOBRE_TIPO IN ('dentista','paciente','atendimento'));

COMMIT;
