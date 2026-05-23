-- ============================================================
-- Migration: notificações com destinatário explícito + leitura
-- Data: 2026-05-23
-- ============================================================
-- Adiciona suporte a:
--   - Notificação direcionada a paciente (dentista → paciente)
--   - Timestamp de leitura (col → den marcado como lido pelo dentista)
--
-- Convenção de direção (sem coluna extra, deduzida pelas FKs):
--   - idColaborador + idDentista   → col → den (paciente fica null)
--   - idDentista    + idPaciente   → den → pac (colaborador fica null)
--   - qualquer outra combinação    → rejeitada no BO
-- ============================================================

-- 1) Coluna paciente como FK opcional
ALTER TABLE T_NOTIFICACAO
  ADD T_PACIENTE_ID_PACIENTE NUMBER NULL;

ALTER TABLE T_NOTIFICACAO
  ADD CONSTRAINT T_NOTIFICACAO_T_PACIENTE_FK
  FOREIGN KEY (T_PACIENTE_ID_PACIENTE)
  REFERENCES T_PACIENTE (ID_PACIENTE);

-- 2) Timestamp de leitura — NULL significa "não lida"
ALTER TABLE T_NOTIFICACAO
  ADD DATA_LEITURA TIMESTAMP NULL;

COMMIT;
