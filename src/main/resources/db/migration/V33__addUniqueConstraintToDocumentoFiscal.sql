-- Migration: Adiciona constraint UNIQUE ao campo documento_fiscal da tabela usuario
-- Objetivo: Garantir unicidade de CPF no nível do banco de dados
-- Data: 29/01/2026

ALTER TABLE usuario 
ADD CONSTRAINT uk_usuario_documento_fiscal UNIQUE (documento_fiscal);
