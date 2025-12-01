import { test, expect } from '@playwright/test';
import * as dotenv from 'dotenv';

dotenv.config();

//Page is responsive
test('TicketGuru responds', async ({ page }) => {
    const url = String(process.env.URL);

    await page.goto(url);
    await expect(page.getByText('pääsylippusi')).toBeVisible();
})

//Unauthenticated users only see login button
test('Unauthenticated right elements shown', async ({ page }) => {
    const url = String(process.env.URL);

    //homepage
    await page.goto(url);

    //homepage element checks
    await expect(page.getByRole('link', { name: 'Login' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Sell Tickets' })).toHaveCount(0);
    await expect(page.getByRole('link', { name: 'Ticket Reader' })).toHaveCount(0);
    await expect(page.getByRole('link', { name: 'Venues' })).toHaveCount(0);
    await expect(page.getByRole('link', { name: 'Events' })).toHaveCount(0);
    await expect(page.getByRole('link', { name: 'Ticket types' })).toHaveCount(0);
    await expect(page.getByRole('link', { name: 'Costs' })).toHaveCount(0);
    await expect(page.getByRole('link', { name: 'Tickets' })).toHaveCount(0);
    await expect(page.getByRole('link', { name: 'Users' })).toHaveCount(0);

})

//Users with USER role don't see USERS link
test('User right elements shown', async ({ page }) => {
    const url = String(process.env.URL);

    //home page
    await page.goto(url);

    //login page
    await page.getByRole('link', { name: 'Login' }).click();
    await expect(page.getByRole('button', { name: 'Sign in' })).toBeVisible();

    //login form
    await page.getByLabel('username').fill(String(process.env.USERNAME2));
    await page.getByLabel('password').fill(String(process.env.PASSWORD2));
    await page.getByRole('button', { name: 'Sign in' }).click();

    //homepage element checks
    await expect(page.getByRole('link', { name: 'Sell Tickets' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Ticket Reader' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Venues' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Events' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Ticket types' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Costs' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Tickets', exact: true })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Users' })).toHaveCount(0);

})

//Users with ADMIN role see all links
test('Admin right elements shown', async ({ page }) => {
    const url = String(process.env.URL);

    //homepage
    await page.goto(url);

    //login page
    await page.getByRole('link', { name: 'Login' }).click();
    await expect(page.getByRole('button', { name: 'Sign in' })).toBeVisible();

    //login form
    await page.getByLabel('username').fill(String(process.env.USERNAME1));
    await page.getByLabel('password').fill(String(process.env.PASSWORD1));
    await page.getByRole('button', { name: 'Sign in' }).click();

    //homepage elemnent checks
    await expect(page.getByRole('link', { name: 'Sell Tickets' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Ticket Reader' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Venues' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Events' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Ticket types' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Costs' })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Tickets', exact: true })).toHaveCount(1);
    await expect(page.getByRole('link', { name: 'Users' })).toHaveCount(1);
    await page.getByRole('button', { name: 'Logout' }).click();
    await expect(page).toHaveURL(`${url}/login?logout`);

})