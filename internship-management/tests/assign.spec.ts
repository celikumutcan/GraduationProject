import { test, expect } from '@playwright/test';

test.describe('Manual Instructor Assignment', () => {
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

    // Navigate to assign instructors page
    await page.waitForURL('http://localhost:4200/coordinator/announcements');
    await page.click('a[href="/coordinator/assign-instructors"]');
    await page.waitForURL('http://localhost:4200/coordinator/assign-instructors');
  });

  test('Should manually assign instructor to trainee form', async ({ page }) => {
    // Mock API responses
    await page.route('http://localhost:8080/api/academicStaff/all', route => {
      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify([
          { userName: 'inst1', firstName: 'John', lastName: 'Doe' },
          { userName: 'inst2', firstName: 'Jane', lastName: 'Smith' }
        ])
      });
    });

    await page.route('http://localhost:8080/api/assignments/assign-manually', route => {
      const requestData = route.request().postDataJSON();
      expect(requestData.id).toBeTruthy();
      expect(requestData.instructorUsername).toBeTruthy();

      route.fulfill({
        status: 200,
        contentType: 'application/json',
        body: JSON.stringify({ success: true })
      });
    });

    // Wait for forms and instructors to load
    await page.waitForSelector('table tbody tr');

    // Get the first form row
    const firstFormRow = page.locator('table tbody tr').first();

    // Select an instructor from dropdown
    await firstFormRow.locator('select').selectOption({ index: 1 }); // Select second instructor

    // Click assign button
    await firstFormRow.locator('button:has-text("Assign")').click();

    await page.waitForTimeout(10000); // Small delay for UI update

    const successPopup = page.locator('div[class*="fixed inset-0 bg-black bg-opacity-50"]');
    await expect(successPopup).toBeVisible();
    await expect(successPopup.locator('h2:text("Success")')).toBeVisible();
    await expect(successPopup.locator('p:text("Application successful!")')).toBeVisible();

    // Close success popup
    await successPopup.locator('button:has-text("OK")').click();
    await expect(successPopup).not.toBeVisible();
  });



});
