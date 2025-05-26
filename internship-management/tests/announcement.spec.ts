import { test, expect } from '@playwright/test';

test.describe('Announcements Integration Tests', () => {
  let coordinatorCredentials = {
    username: 'cidil',
    password: 'cidil2'
  };

  test.beforeEach(async ({ page }) => {
    // Login as coordinator (who has access to announcements)
    await page.goto('http://localhost:4200');
    await page.fill('input[id="username"]', coordinatorCredentials.username);
    await page.fill('input[id="password"]', coordinatorCredentials.password);
    await page.click('button[type="submit"]');

    await page.waitForURL('http://localhost:4200/coordinator/announcements');
  });

  test('Should load existing announcements', async ({ page }) => {
    // Verify announcements are displayed
    const announcements = page.locator('div.bg-white.dark\\:bg-gray-800');
    await expect(announcements).not.toHaveCount(0);

    // Check announcement structure
    const firstAnnouncement = announcements.first();
    await expect(firstAnnouncement.locator('h3.text-xl')).toBeVisible();
    await expect(firstAnnouncement.locator('p.text-sm')).toBeVisible();
    await expect(firstAnnouncement.locator('.announcement-footer')).toBeVisible();
  });

  test('Should show/hide add announcement form', async ({ page }) => {
    // Form should be hidden initially
    await expect(page.locator('div[class*="bg-gray-50"]')).not.toBeVisible();

    // Click add button and verify form appears
    await page.click('button:has-text("Add Announcement")');
    await expect(page.locator('div[class*="bg-gray-50"]')).toBeVisible();

    // Click cancel and verify form disappears
    await page.click('button:has-text("Cancel")');
    await expect(page.locator('div[class*="bg-gray-50"]')).not.toBeVisible();
  });

  test('Should create new announcement', async ({ page }) => {
    // Open add form
    await page.click('button:has-text("Add Announcement")');

    // Fill in form
    const testTitle = 'Test Announcement3 ' + Date.now();
    const testContent = 'This is a test announcement content';

    await page.fill('input[id="title"]', testTitle);
    await page.fill('textarea[id="content"]', testContent);

    // Select roles
    await page.check('input[value="Students"]');
    await page.check('input[value="Instructors"]');

    // Mock the API response (or use real API)
    await page.route('http://localhost:8080/api/announcements', route => {
      if (route.request().method() === 'POST') {
        route.fulfill({
          status: 200,
          contentType: 'application/json',
          body: JSON.stringify({ success: true })
        });
      } else {
        route.continue();
      }
    });

    // Submit form
    await page.click('button:has-text("Save")');

    // Verify form closes after submission
    await expect(page.locator('div[class*="bg-gray-50"]')).not.toBeVisible();

    // Verify new announcement appears in list (if using real API)
     //await expect(page.locator(`h3:text("${testTitle}")`)).toBeVisible();
  });



});
