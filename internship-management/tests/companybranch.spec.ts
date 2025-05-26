import { test, expect } from '@playwright/test';

// Test credentials for company branch user
const companyCredentials = {
  username: 'Google-Branch',
  password: 'Google-Branch1'
};

test.describe('Form Approval and Supervisor Assignment', () => {
  test.beforeEach(async ({ page }) => {
    // Login as company branch user
    await page.goto('http://localhost:4200/company-branch/login');
    await page.fill('input[id="username"]', companyCredentials.username);
    await page.fill('input[id="password"]', companyCredentials.password);
    await page.click('button:has-text("LOGIN")');

    // Navigate to evaluate intern student page
    await page.waitForURL('http://localhost:4200/company-branch');
    await page.click('a[href="/company-branch/evaluate-intern-student"]');
    await page.waitForURL('http://localhost:4200/company-branch/evaluate-intern-student');
  });

  test('Should approve internship form and verify status change', async ({ page }) => {
    // Wait for the table to load
    await page.waitForSelector('table.w-full');

    // Find a form with "Waiting For Approval" status
    const waitingForm = page.locator('td.neutral-status:has-text("Waiting For Approval")').first();
    await expect(waitingForm).toBeVisible({ timeout: 10000 });

    // Get the parent row
    const formRow = waitingForm.locator('xpath=./ancestor::tr');

    // Get the form ID from the row (if needed for debugging)
    const formId = await formRow.getAttribute('data-id'); // Add data-id attribute to your rows if needed

    // Click the Approve button
    const approveButton = formRow.locator('button:has-text("Accept")');
    await approveButton.click();

    // Verify toast notification appears
    await expect(page.locator('div.fixed.bottom-4.right-4:has-text("Internship successfully approved!")')).toBeVisible();
  });

  test('Should set supervisor and verify display', async ({ page }) => {
    await page.reload();
    const allForms = page.locator('tbody tr');
    const formRow = allForms.nth(1);


    const supervisorCell = formRow.locator('td').nth(2); // 3rd column is supervisor
    await expect(supervisorCell).toHaveText(/^\s*$/); // Matches empty or whitespace-only

    // Click "Set Supervisor" button
    await formRow.locator('button.bg-emerald-600').click();

    // Fill in supervisor details
    const supervisorName = "Ka Ryo";
    const supervisorSurname = "Ten";

    const modal = page.locator('div.modal-container');
    await expect(modal).toBeVisible();

// Fill the fields reliably
    const nameInput = modal.locator('input').first();
    const surnameInput = modal.locator('input').nth(1);

    await expect(nameInput).toBeEditable();
    await nameInput.fill("Ka Ryo");

    await expect(surnameInput).toBeEditable();
    await surnameInput.fill("Ten");

    // Submit the form
    await page.click('button:has-text("Submit")');

    // Wait for modal to close
    await expect(page.locator('div.fixed.inset-0.bg-black')).not.toBeVisible();

    // Verify supervisor name is now displayed in the table
    await expect(formRow.locator(`td:has-text("${supervisorName} ${supervisorSurname}")`)).toBeVisible();

    // Verify the "Unassigned" text is gone
    await expect(formRow.locator('td:has-text("Unassigned")')).not.toBeVisible();
  });
});
