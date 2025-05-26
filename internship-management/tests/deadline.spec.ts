import { test, expect } from '@playwright/test';

test.describe('Deadline Tests', () => {
  const coordinatorCredentials = {
    username: 'cidil',
    password: 'cidil2'
  };

  test.beforeEach(async ({ page }) => {
    // Login as coordinator
    await page.goto('http://localhost:4200');
    await page.fill('input[id="username"]', coordinatorCredentials.username);
    await page.fill('input[id="password"]', coordinatorCredentials.password);
    await page.click('button[type="submit"]');

    // Navigate to deadlines page
    await page.waitForURL('http://localhost:4200/coordinator/announcements');
    await page.click('a[href="/coordinator/set-deadlines"]');
    await page.waitForURL('http://localhost:4200/coordinator/set-deadlines');
  });

  test('Should set report deadline successfully', async ({ page }) => {
    await page.click('a[href="/coordinator/set-deadlines"]');
    await page.waitForURL('http://localhost:4200/coordinator/set-deadlines');
    // Mock the API response for saving deadline
    await page.route('http://localhost:8080/api/deadlines/add', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({ success: true })
      });
    });

    // Mock the API response for getting updated deadline
    await page.route('http://localhost:8080/api/deadlines/report-deadline', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({ reportDeadline: '2024-12-31' })
      });
    });

    // Set a future date for testing
    const futureDate = new Date();
    futureDate.setMonth(futureDate.getMonth() + 3);
    const formattedDate = futureDate.toISOString().split('T')[0];

    // Fill in the report deadline date - using nth(1) to select the second date input
    const reportDateInput = page.locator('input[type="date"]').nth(1);
    await reportDateInput.fill(formattedDate);

    // Click save button - using nth(1) to select the second save button
    await page.locator('button:has-text("Save")').nth(1).click();

    // Verify the current deadline updates - using nth(1) to select the second deadline display
    await expect(page.locator('span.text-red-500').nth(1)).toHaveText('2024-12-31');
  });

  test('Should remove report deadline successfully', async ({ page }) => {

    // Click remove button - using nth(1) to select the second remove button
    await page.locator('button:has-text("Remove Deadline")').nth(1).click();

    // Verify the deadline is removed
    await expect(page.locator('span.text-red-500').nth(1)).toHaveText('Not Set');
  });
});
