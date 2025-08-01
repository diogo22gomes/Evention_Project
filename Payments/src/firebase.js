import fs from 'fs';
import path from 'path';
import admin from 'firebase-admin';
const serviceAccountPath = path.resolve('src/serviceAccountKey.json');
const serviceAccount = JSON.parse(fs.readFileSync(serviceAccountPath, 'utf-8'));

if (!admin.apps.length) {
  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
  });
}

export default admin;
