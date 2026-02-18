-- Migration: Adiciona campo 'reservavel' à tabela equipamento
-- Objetivo: Sinalizar se o equipamento está disponível para reserva
-- Data: 18/02/2026

ALTER TABLE equipamento 
ADD COLUMN reservavel BOOLEAN NOT NULL DEFAULT true;

COMMENT ON COLUMN equipamento.reservavel IS 'Indica se o equipamento está disponível para ser reservado';
