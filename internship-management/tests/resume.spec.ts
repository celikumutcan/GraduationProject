import { test, expect } from '@playwright/test';

test.describe('Resume Management Integration Tests', () => {
  const studentCredentials = {
    username: 'e258546',
    password: 'efekan1'
  };
  const resumePath = 'C:/Users/uysal/Downloads/cv2.pdf';

  test.beforeEach(async ({ page }) => {
    // Login as student
    await page.goto('http://localhost:4200');
    await page.fill('input[id="username"]', studentCredentials.username);
    await page.fill('input[id="password"]', studentCredentials.password);
    await page.click('button[type="submit"]');

    // Navigate to resume page
    await page.waitForURL('http://localhost:4200/student');
    await page.goto('http://localhost:4200/student/my-resume');
  });

  test('Should upload a resume successfully', async ({ page }) => {
    // Wait for resume section to load
    await page.waitForSelector('div.upload-container');

    // Trigger file upload
    const [fileChooser] = await Promise.all([
      page.waitForEvent('filechooser'),
      page.click('button:has-text("Browse")')
    ]);
    await fileChooser.setFiles(resumePath);

    // Wait for upload to complete
    await page.waitForSelector('div.bg-white.dark\\:bg-gray-800');

    // Verify resume is displayed
    const fileName = await page.locator('span.text-gray-800').textContent();
    expect(fileName).toContain('cv2.pdf');

    // Verify buttons are visible
    await expect(page.locator('button:has-text("Download")')).toBeVisible();
    await expect(page.locator('button:has-text("Delete")')).toBeVisible();
  });

  test('Should stay uploaded after reloading the page', async ({ page }) => {
    test.setTimeout(60000);

    // Reload the page to verify persistence
    await page.reload();
    await page.waitForSelector('div.bg-white.dark\\:bg-gray-800');

    // Verify resume is still deleted after reload
    await expect(page.locator('div.bg-white.dark\\:bg-gray-800')).toBeVisible();

    // Additional check - verify no download button exists
    await expect(page.locator('button:has-text("Download")')).toBeVisible();
  });
});
