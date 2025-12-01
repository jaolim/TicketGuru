import { test, expect, Page } from '@playwright/test';
import { trClickLinkIfExists, trClickButtonIfExists } from '../helpers/utils';
import * as dotenv from 'dotenv';

dotenv.config();

 let page: Page;

 //Login for tests session
 test.beforeAll(async ({ browser}) => {
    const context = await browser.newContext();
    page = await context.newPage();
    const url = String(process.env.URL);

    //homepage
    await page.goto(url);

    //login
    await page.getByRole('link', { name: 'Login' }).click();
    await expect(page.getByRole('button', { name: 'Sign in' })).toBeVisible();
    await page.getByLabel('username').fill(String(process.env.USERNAME1));
    await page.getByLabel('password').fill(String(process.env.PASSWORD1));
    await page.getByRole('button', { name: 'Sign in' }).click();

 })

//Venues add, edit and deletion
test('Venues CRUD', async ( ) => {
    const url = String(process.env.URL);

    //homepage
    await page.goto(url);
    
    //Venues page
    await page.getByRole('link', { name: 'Venues' }).click();
    await expect(page).toHaveURL(`${url}/venuepage`);

    //Clearing leftovers
    page.once('dialog', async dialog => {
        await dialog.accept();
    });
    await trClickButtonIfExists(page, 'Playwright', 'Delete');
    await trClickButtonIfExists(page, 'Qwerty321', 'Delete');

    //Add venue page
    await page.getByRole('link', { name: 'Add New Venue' }).click();
    await expect(page).toHaveURL(`${url}/venue/add`);

    //Back button
    await page.getByRole('link', { name: 'Back' }).click();
    await expect(page).toHaveURL(`${url}/venuepage`);

    //Add page error
    await page.getByRole('link', { name: 'Add New Venue' }).click();
    await page.getByLabel('address').fill('test123');
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page).toHaveURL(`${url}/venue/add`);

    //Add works
    await page.getByLabel('name').fill('Playwright');
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByText('test123')).toBeVisible();

    //Edit works
    await page
        .locator('tr', { has: page.getByText('Playwright') })
        .getByRole('link', { name: 'Edit' })
        .click();
    await page.getByLabel('name').fill('Qwerty321');
    await page.getByLabel('address').fill('332211');
    await page.getByRole('button', { name: 'Save' }).click();
    await expect(page.getByText('332211')).toBeVisible();

    //Delete element
    const del = page
        .locator('tr', { has: page.getByText('Qwerty321') })
        .getByRole('button', { name: 'Delete' })
    await expect(del).toHaveCount(1);

    //Delete works
    await del.click();
    await expect(page.getByText('Playwright')).toHaveCount(0);
    await expect(page.getByText('Qwerty321')).toHaveCount(0);

})