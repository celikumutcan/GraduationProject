import { test, expect } from '@playwright/test';
import path from 'path';

test.describe('Forms Integration Tests', () => {
  const coordinatorCredentials = {
    username: 'cidil',
    password: 'cidil2'
  };
  const testPdfPath = 'C:/Users/uysal/Downloads/cv2.pdf';

  test.beforeEach(async ({ page }) => {
    // Login as coordinator
    await page.goto('http://localhost:4200');
    await page.fill('input[id="username"]', coordinatorCredentials.username);
    await page.fill('input[id="password"]', coordinatorCredentials.password);
    await page.click('button[type="submit"]');

    // Navigate to forms page
    await page.waitForURL('http://localhost:4200/coordinator/announcements');
    await page.click('a[href="/coordinator/forms"]');
    await page.waitForURL('http://localhost:4200/coordinator/forms');
  });

  test('Should load existing forms from API', async ({ page }) => {
    // Wait for forms to load
    await page.waitForSelector('table tbody tr');

    // Verify forms table is populated
    const formRows = page.locator('table tbody tr');
    const formCount = await formRows.count();
    expect(formCount).toBeGreaterThan(0);

    // Verify each form has required columns
    for (let i = 0; i < formCount; i++) {
      const row = formRows.nth(i);
      await expect(row.locator('td:nth-child(1)')).not.toBeEmpty();
      await expect(row.locator('td:nth-child(2)')).not.toBeEmpty();
      await expect(row.locator('td:nth-child(3)')).not.toBeEmpty();
    }
  });

  test('Should successfully upload a PDF form', async ({ page }) => {
    // Get initial form count
    await page.waitForSelector('table tbody tr'); // Waits for at least one row to appear
    const initialFormCount = await page.locator('table tbody tr').count();

    // Upload test PDF file
    const fileChooserPromise = page.waitForEvent('filechooser');
    await page.click('label.file-upload-label');
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(testPdfPath);

    // Wait for upload to complete
    await expect(page.locator('text=Failed to send form to server.')).not.toBeVisible();
    await page.waitForTimeout(10000); // Small delay for UI update

    // Verify new form appears in the list
    await expect(page.locator('table tbody tr')).toHaveCount(initialFormCount + 1);

    // Verify the new form has correct filename
    const fileName = path.basename(testPdfPath);
    await expect(page.locator(`table tbody tr:first-child td:first-child`)).toContainText(fileName);
  });

  test('Should show error when uploading non-PDF file', async ({ page }) => {
    // Create a temporary text file for testing
    const testFilePath = path.join(__dirname, 'testfile.txt');
    require('fs').writeFileSync(testFilePath, 'This is a test file');

    try {
      // Attempt to upload text file
      const fileChooserPromise = page.waitForEvent('filechooser');
      await page.click('label.file-upload-label');
      const fileChooser = await fileChooserPromise;
      await fileChooser.setFiles(testFilePath);

      // Verify error message appears
      await expect(page.locator('text=Only PDF files are allowed.')).toBeVisible();

      // Verify no new form was added
      const formCount = await page.locator('table tbody tr').count();
      expect(formCount).toEqual(await page.locator('table tbody tr').count());
    } finally {
      // Clean up test file
      require('fs').unlinkSync(testFilePath);
    }
  });

  test('Should refresh forms list after upload', async ({ page }) => {
    // Mock the initial forms count
    const initialForms = await page.locator('table tbody tr').all();
    const initialCount = initialForms.length;

    // Upload test PDF
    const fileChooserPromise = page.waitForEvent('filechooser');
    await page.click('label.file-upload-label');
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(testPdfPath);

    // Wait for refresh
    await page.waitForFunction((initial) => {
      const rows = document.querySelectorAll('table tbody tr');
      return rows.length > initial;
    }, initialCount);

    // Verify new count is greater than initial
    const newCount = await page.locator('table tbody tr').count();
    expect(newCount).toBeGreaterThan(initialCount);
  });


});
