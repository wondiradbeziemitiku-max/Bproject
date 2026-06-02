import * as crypto from 'crypto';

export function generateShuffledPool(): { pool: number[], hash: string } {
  const pool = Array.from({ length: 75 }, (_, i) => i + 1);
  for (let i = pool.length - 1; i > 0; i--) {
    const buf = crypto.randomBytes(4);
    const j = buf.readUInt32BE(0) % (i + 1);
    [pool[i], pool[j]] = [pool[j], pool[i]];
  }
  const hash = crypto.createHash('sha256').update(pool.join(',')).digest('hex');
  return { pool, hash };
}
