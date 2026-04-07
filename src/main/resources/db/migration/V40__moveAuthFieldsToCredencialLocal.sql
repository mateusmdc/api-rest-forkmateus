ALTER TABLE credencial_local
    ADD COLUMN access_failed_count INT DEFAULT 0,
    ADD COLUMN lockout_enabled BOOLEAN DEFAULT FALSE,
    ADD COLUMN lockout_end TIMESTAMP,
    ADD COLUMN token_expiration TIMESTAMP,
    ADD COLUMN token_mail VARCHAR(255);

UPDATE credencial_local cl
SET access_failed_count = COALESCE(u.access_failed_count, 0),
    lockout_enabled = COALESCE(u.lockout_enabled, FALSE),
    lockout_end = u.lockout_end,
    token_expiration = u.token_expiration,
    token_mail = u.token_mail
    FROM usuario u
WHERE cl.usuario_id = u.id;

ALTER TABLE usuario
DROP COLUMN access_failed_count,
    DROP COLUMN lockout_enabled,
    DROP COLUMN lockout_end,
    DROP COLUMN token_expiration,
    DROP COLUMN token_mail;