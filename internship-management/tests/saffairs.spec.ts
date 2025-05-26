import { test, expect } from '@playwright/test';

test.describe('Student Affairs Insurance Approval', () => {
  const studentAffairsCredentials = {
    username: 's_affairs',
    password: 'student_affairs1'
  };

  test.beforeEach(async ({ page }) => {
    // Login as student affairs officer
    await page.goto('http://localhost:4200');
    await page.fill('input[id="username"]', studentAffairsCredentials.username);
    await page.fill('input[id="password"]', studentAffairsCredentials.password);
    await page.click('button[type="submit"]');

    // Navigate to approved internships page
    await page.waitForURL('http://localhost:4200/student-affairs');
    await page.click('a[href="/student-affairs/approved-internships"]');
    await page.waitForURL('http://localhost:4200/student-affairs/approved-internships');
  });

  test('Should approve insurance for an internship', async ({ page }) => {
    // Wait for internships to load
    await page.waitForSelector('table');

    // Find the first internship with "Approve Insurance" button
    const approveButton = page.locator('button:has-text("Approve Insurance")').first();
    await expect(approveButton).toBeVisible();

    // Get internship details before approval
    const internshipRow = approveButton.locator('xpath=ancestor::tr');
    const studentName = await internshipRow.locator('td:nth-child(1)').textContent();
    const companyName = await internshipRow.locator('td:nth-child(3)').textContent();

    // Click approve button
    await approveButton.click();

    // Refresh the page to verify persistence
    await page.reload();
    await page.waitForSelector('table');

    // Verify the internship still shows as approved
    const refreshedRow = page.locator(`tr:has(td:text("${studentName}")):has(td:text("${companyName}"))`);
    await expect(refreshedRow.locator('text=Approved')).toBeVisible();
  });


  test('Should persist approval status after page refresh', async ({ page }) => {
    // 1. Locate the first unapproved internship
    const unapprovedRow = page.locator('tr:has(button:has-text("Approve Insurance"))').first();
    await expect(unapprovedRow).toBeVisible();

    // Get identifying information before approval
    const studentName = await unapprovedRow.locator('td:nth-child(1)').textContent();
    const companyName = await unapprovedRow.locator('td:nth-child(3)').textContent();

    // 3. Click the approve button
    await unapprovedRow.locator('button:has-text("Approve Insurance")').click();

    await page.waitForTimeout(4000); // Small delay for UI update

    // 5. Reload the page to verify persistence
    await page.reload();
    await page.waitForSelector('table');

    // 6. Find the same internship by student/company info
    const refreshedRow = page.locator(`tr:has(td:text("${studentName}")):has(td:text("${companyName}"))`);
    await expect(refreshedRow).toBeVisible();

    // 7. Verify it remains approved after refresh
    await expect(refreshedRow.locator('button:has-text("Approve Insurance")')).not.toBeVisible();
  });
});
