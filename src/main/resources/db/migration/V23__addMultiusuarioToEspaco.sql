-- Adiciona campo multiusuario à tabela espaco
-- Indica se o espaço pode ser utilizado por diferentes tipos de usuários

ALTER TABLE espaco 
ADD COLUMN multiusuario BOOLEAN NOT NULL DEFAULT FALSE;

-- Adiciona comentário para documentar o campo
COMMENT ON COLUMN espaco.multiusuario IS 'Indica se o espaço é do tipo "Multiusuário" e pode ser utilizado por diferentes tipos de usuários';
