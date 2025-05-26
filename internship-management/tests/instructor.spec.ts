import { test, expect } from '@playwright/test';

test.describe('Reports and Company Evaluation Integration Tests', () => {
  let instructorCredentials = {
    username: 'eever',
    password: 'eever1'
  };

  test.beforeEach(async ({ page }) => {
    // Login as instructor
    await page.goto('http://localhost:4200');
    await page.fill('input[id="username"]', instructorCredentials.username);
    await page.fill('input[id="password"]', instructorCredentials.password);
    await page.click('button[type="submit"]');

    // Navigate to evaluate reports page
    await page.waitForURL('http://localhost:4200/instructor');
    await page.click('a[href="/instructor/evaluate-assigned-reports"]');
    await page.waitForURL('http://localhost:4200/instructor/evaluate-assigned-reports');
  });

  test('Should display and navigate through student reports', async ({ page }) => {
    test.setTimeout(60000);

    await page.waitForTimeout(3000); // Small delay for UI update

    // Wait for forms to load
    await page.waitForSelector('table.w-full');

    // Get first form with reports
    const allForms = page.locator('tbody tr');
    const formWithReports = allForms.nth(4);

    // Click the "Reports" button
    await formWithReports.locator('button:has-text("Reports")').click();

    // Wait for reports modal to appear
    await page.waitForSelector('div.fixed.inset-0.z-50');


    await page.waitForTimeout(3000);
    // Verify reports table is visible
    const reportsTable = page.locator('div.fixed.inset-0.z-50 table.w-full');
    await expect(reportsTable).toBeVisible();

    // Check if reports exist or "No reports" message appears
    const noReportsMessage = page.locator('text="No reports uploaded."');

    if (await noReportsMessage.isVisible()) {
      await expect(noReportsMessage).toBeVisible();
    } else {
      const reports = reportsTable.locator('tbody tr');
      await expect(reports).not.toHaveCount(0);

      // Check report details
      const firstReport = reports.first();
      await expect(firstReport.locator('td:has(a)')).toBeVisible(); // File link
      await expect(firstReport.locator('td:nth-child(2)')).not.toBeEmpty(); // Date
      await expect(firstReport.locator('td:nth-child(3)')).not.toBeEmpty(); // Status
      await expect(firstReport.locator('td:nth-child(4)')).not.toBeEmpty(); // Grade
    }
    // Close reports modal
    await page.locator('button:has-text("X")').click();
    await expect(page.locator('div.fixed.inset-0.z-50')).not.toBeVisible();
  });

  test('Should display submitted company evaluation', async ({ page }) => {
    // Wait for forms to load
    await page.waitForSelector('table.w-full');

    // Get first form with submitted company evaluation
    const formWithEval = page.locator('tr:has(td.positive-status)').first();
    await expect(formWithEval).toBeVisible();

    // Click the company evaluation link
    await formWithEval.locator('a.positive-status').click();

    // Wait for company evaluation modal to appear
    await page.waitForSelector('div.fixed.inset-0.z-50');

    // Verify evaluation form is visible
    const evaluationForm = page.locator('form');
    await expect(evaluationForm).toBeVisible();

    // Check evaluation criteria (should be populated from backend)
    const attendanceField = page.locator('input[name="attendance"]:checked');
    await expect(attendanceField).toHaveValue(/Excellent|Good|Satisfactory|Unsatisfactory/);

    const diligenceField = page.locator('input[name="diligence"]:checked');
    await expect(diligenceField).toHaveValue(/Excellent|Good|Satisfactory|Unsatisfactory/);

    const contributionField = page.locator('input[name="contribution"]:checked');
    await expect(contributionField).toHaveValue(/Excellent|Good|Satisfactory|Unsatisfactory/);

    const performanceField = page.locator('input[name="performance"]:checked');
    await expect(performanceField).toHaveValue(/Excellent|Good|Satisfactory|Unsatisfactory/);

    // Check comments field
    const commentsField = page.locator('textarea[name="comments"]');
    await expect(commentsField).not.toBeEmpty();

    // Close reports modal
    await page.locator('button:has-text("X")').click();
    await expect(page.locator('div.fixed.inset-0.z-50')).not.toBeVisible();
  });
});
