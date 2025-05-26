import { test, expect } from '@playwright/test';

// Test data for different user types
const testUsers = [
  {
    username: 'cidil',
    password: 'cidil2',
    userType: 'coordinator',
    expectedRoute: '/coordinator/announcements'
  },
  {
    username: 'e258546',
    password: 'efekan1',
    userType: 'student',
    expectedRoute: '/student'
  },
  {
    username: 'eever',
    password: 'eever1',
    userType: 'instructor',
    expectedRoute: '/instructor'
  },
  {
    username: 's_affairs',
    password: 'student_affairs1',
    userType: 'student_affairs',
    expectedRoute: '/student-affairs'
  },
  {
    username: 'Google-Branch',
    password: 'Google-Branch1',
    userType: 'company_branch',
    expectedErrorMessage: 'Company users must log in from the Company Login screen.'
  }
];

test.describe('Frontend-Backend Integration Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to the login page before each test
    await page.goto('http://localhost:4200');
  });

  test('Successful login and routing for different user types', async ({ page }) => {
    for (const user of testUsers) {
      // Fill in the login form
      await page.fill('input[id="username"]', user.username);
      await page.fill('input[id="password"]', user.password);
      await page.click('button[type="submit"]');

      if (user.expectedRoute) {
        // Check if we're redirected to the correct route
        await page.waitForURL(`http://localhost:4200${user.expectedRoute}`);
        expect(page.url()).toBe(`http://localhost:4200${user.expectedRoute}`);

        // Go back to login page for next test
        await page.goto('http://localhost:4200');
      } else if (user.expectedErrorMessage) {
        // Check for error message for company users
        await expect(page.locator('div.text-red-600')).toContainText(user.expectedErrorMessage);
      }
    }
  });

  test('Invalid credentials show error message', async ({ page }) => {
    // Test with wrong credentials
    await page.fill('input[id="username"]', 'invaliduser');
    await page.fill('input[id="password"]', 'wrongpassword');
    await page.click('button[type="submit"]');

    // Check for error message
    await expect(page.locator('div.text-red-600')).toContainText('You have entered wrong username or password');
  });

  test('Empty form submission shows validation errors', async ({ page }) => {
    // Submit form without filling anything
    await page.click('button[type="submit"]');

    // Check that we're still on the login page
    expect(page.url()).toBe('http://localhost:4200/');

    // The required fields should show validation errors (though you might want to add visual indicators)
    const usernameInput = page.locator('input[id="username"]');
    const passwordInput = page.locator('input[id="password"]');

    await expect(usernameInput).toHaveJSProperty('validity.valid', false);
    await expect(passwordInput).toHaveJSProperty('validity.valid', false);
  });

  test('Company login link redirects and allows successful company login', async ({ page }) => {
    // Click the company login link
    await page.click('a[href="/company-branch/login"]');

    // Check if we're redirected to the company login page
    await page.waitForURL('http://localhost:4200/company-branch/login');
    expect(page.url()).toBe('http://localhost:4200/company-branch/login');

    // Fill in company login credentials
    await page.fill('input[id="username"]', 'Google-Branch');
    await page.fill('input[id="password"]', 'Google-Branch1');

    // Click the login button and wait for navigation
    await Promise.all([
      page.waitForURL('http://localhost:4200/company-branch'),
      page.click('button:has-text("Login")')
    ]);

    // Verify we're redirected to company dashboard
    expect(page.url()).toBe('http://localhost:4200/company-branch/my-offers');

    // Verify localStorage items were set
    const token = await page.evaluate(() => localStorage.getItem('token'));
    expect(token).toBeTruthy();

    const currentUser = await page.evaluate(() => {
      const user = localStorage.getItem('currentUser');
      return user ? JSON.parse(user) : null;
    });
    expect(currentUser).toBeTruthy();
    expect(currentUser.userType).toBe('company_branch');
    expect(currentUser.userName).toBe('Google-Branch');

    const companyBranchId = await page.evaluate(() => localStorage.getItem('companyBranchId'));
    expect(companyBranchId).toBeTruthy();
  });
});
