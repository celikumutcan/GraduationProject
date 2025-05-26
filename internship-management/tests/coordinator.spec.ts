import { test, expect } from '@playwright/test';

test.describe('Trainee Forms Evaluation Integration Tests', () => {
  let coordinatorCredentials = {
    username: 'cidil',
    password: 'cidil2'
  };

  test.beforeEach(async ({ page }) => {
    // Login as coordinator
    await page.goto('http://localhost:4200');
    await page.fill('input[id="username"]', coordinatorCredentials.username);
    await page.fill('input[id="password"]', coordinatorCredentials.password);
    await page.click('button[type="submit"]');

    // Navigate to evaluate forms page
    await page.waitForURL('http://localhost:4200/coordinator/announcements');
    await page.click('a[href="/coordinator/evaluate-forms"]');
    await page.waitForURL('http://localhost:4200/coordinator/evaluate-forms');
  });

  test('Should display trainee forms data correctly', async ({ page }) => {
    // Verify table structure and headers
    await expect(page.locator('table')).toBeVisible();

    // More precise header selectors using nth-child
    const headerSelectors = [
      'th.border-b:nth-child(2)', // Name
      'th.border-b:nth-child(3)', // Surname
      'th.border-b:nth-child(4)', // Student No
      'th.border-b:nth-child(5)', // Course
      'th.border-b:nth-child(6)', // Evaluating Faculty
      'th.border-b:nth-child(7)', // Coordinator Approval
      'th.border-b:nth-child(8)', // Company Approval
      'th.border-b:nth-child(9)', // Supervisor Evaluation
      'th.border-b:nth-child(10)' // Actions
    ];

    for (const selector of headerSelectors) {
      await expect(page.locator(selector)).toBeVisible();
    }

    // Check if forms are loaded
    const forms = page.locator('tbody tr');
    await expect(forms).not.toHaveCount(0);

    // Verify data in the first row
    const firstRow = forms.first();
    await expect(firstRow.locator('td:nth-child(2)')).not.toBeEmpty(); // Name
    await expect(firstRow.locator('td:nth-child(3)')).not.toBeEmpty(); // Surname
    await expect(firstRow.locator('td:nth-child(4)')).not.toBeEmpty(); // Student No
    await expect(firstRow.locator('td:nth-child(5)')).not.toBeEmpty(); // Course

    // Verify status columns have valid values
    const coordinatorStatus = await firstRow.locator('td:nth-child(7)').textContent();
    expect(['Not Approved', 'Approved']).toContain(coordinatorStatus?.trim());

    const companyStatus = await firstRow.locator('td:nth-child(8)').textContent();
    expect(['Not Approved', 'Approved']).toContain(companyStatus?.trim());
  });

  test('Should approve first coordinator approval waiting form', async ({ page }) => {
    test.setTimeout(60000);
    // Find all forms with "Not Approved" status in Coordinator Approval column
    const coordinatorApprovalCells = page.locator('td:nth-child(7).negative-status:has-text("Not Approved")');

    // Verify there are forms waiting for approval
    await expect(coordinatorApprovalCells).not.toHaveCount(0);

    // Get the first row with "Not Approved" status
    const firstNotApprovedRow = coordinatorApprovalCells.first().locator('..'); // Move up to parent row

    // Open details view for this form
    await firstNotApprovedRow.locator('button:has-text("Details")').click();

    // Wait for modal to appear
    const modal = page.locator('div.fixed.inset-0');
    await expect(modal).toBeVisible();

    // Verify approve button is visible (indicates waiting status)
    await expect(modal.locator('button:has-text("Approve")')).toBeVisible();

    // Click approve button
    await modal.locator('button:has-text("Approve")').click();

    // Set up dialog handler for the alert
    page.on('dialog', async dialog => {
      expect(dialog.message()).toContain('Form approved successfully!');
      await dialog.accept();
    });

  });
});
