-- Migration: Adiciona campo 'reservavel' à tabela espaco
-- Objetivo: Sinalizar se o espaço está disponível para reserva
-- Data: 18/02/2026

ALTER TABLE espaco 
ADD COLUMN reservavel BOOLEAN NOT NULL DEFAULT true;

COMMENT ON COLUMN espaco.reservavel IS 'Indica se o espaço está disponível para ser reservado';
