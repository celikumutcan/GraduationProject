import { defineConfig } from '@playwright/test';

export default defineConfig({
  testDir: 'tests', // ✅ Only look here for Playwright tests
  testMatch: 'files.spec.ts'
});

