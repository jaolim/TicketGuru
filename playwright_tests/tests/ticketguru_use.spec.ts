import { test, expect, Page } from '@playwright/test';
import { trClickLinkIfExists, trClickButtonIfExists } from '../helpers/utils';
import * as dotenv from 'dotenv';

dotenv.config();

let page: Page;
const url = String(process.env.URL);

//Login for tests session
test.beforeAll(async ({ browser }) => {
    const context = await browser.newContext();
    page = await context.newPage();

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
test('Venues CRUD', async () => {
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

//Events details, add, edit and deletion
test('Events CRUD', async () => {
    const url = String(process.env.URL);

    //homepage
    await page.goto(url);

    //Events page
    await page.getByRole('link', { name: 'Events' }).click();
    await expect(page).toHaveURL(`${url}/eventpage`);

    //Clearing leftovers
    page.once('dialog', async dialog => {
        await dialog.accept();
    });
    await trClickButtonIfExists(page, 'Playwright', 'Delete');
    await trClickButtonIfExists(page, 'Qwerty321', 'Delete');

    //Add event page
    await page.getByRole('link', { name: 'Add New Event' }).click();
    await expect(page).toHaveURL(`${url}/event/add`);

    //Partial fill not accepted
    const date = new Date().toISOString().slice(0, 16);
    const button = page.getByRole('button', { name: 'save' });
    await page.getByLabel('name').fill('Playwright');
    await button.click()
    await expect(page).toHaveURL(`${url}/event/add`);
    await page.getByLabel('date').fill(date);
    await button.click()
    await expect(page).toHaveURL(`${url}/event/add`);
    await page.locator('#venueId').selectOption({ label: 'Kulttuuritalo' });
    await button.click()
    await expect(page).toHaveURL(`${url}/event/add`);

    //Full fill accepted
    await page.getByLabel('capacity').fill('100');
    await button.click()
    await expect(page).toHaveURL(`${url}/eventpage`);

    //Details visible
    await page
        .locator('tr', { has: page.getByText('Playwright') })
        .getByRole('link', { name: 'Details' })
        .click();
    await expect(page.getByText('Event details for Playwright')).toBeVisible();
    await page.goto(`${url}/eventpage`);

    //Edit works
    await page
        .locator('tr', { has: page.getByText('Playwright') })
        .getByRole('link', { name: 'Edit' })
        .click();
    await page.getByLabel('name').fill('Qwerty321');
    await page.getByRole('button', { name: 'save' }).click();
    await expect(page.getByText('Qwerty321')).toBeVisible();

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

//TicketTypes add, edit and deletion
test('TicketTypes CRUD', async () => {
    const url = String(process.env.URL);

    //homepage
    await page.goto(url);

    //Tickettypes page
    await page.getByRole('link', { name: 'Ticket types' }).click();
    await expect(page).toHaveURL(`${url}/tickettypepage`);

    //Clearing leftovers
    page.once('dialog', async dialog => {
        await dialog.accept();
    });
    await trClickButtonIfExists(page, 'Playwright', 'Delete');
    await trClickButtonIfExists(page, 'Qwerty321', 'Delete');

    //Add ticket type
    await page.getByRole('link', { name: 'Add Ticket type' }).click();
    await expect(page).toHaveURL(`${url}/tickettype/add`);


    //Partial fill not accepted
    const button = page.getByRole('button', { name: 'save' });
    await button.click()
    await expect(page).toHaveURL(`${url}/tickettype/add`);

    //Add works
    await page.getByLabel('name').fill('Playwright');
    await button.click()
    await expect(page).toHaveURL(`${url}/tickettypepage`);
    await expect(page.getByText('Playwright')).toBeVisible();

    //Edit works
    await page
        .locator('tr', { has: page.getByText('Playwright') })
        .getByRole('link', { name: 'Edit' })
        .click();
    await page.getByLabel('name').fill('Qwerty321');
    await page.getByRole('button', { name: 'save' }).click();
    await expect(page.getByText('Qwerty321')).toBeVisible();

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

//Costs add, edit and deletion
test('Costs CRUD', async () => {
    const url = String(process.env.URL);

    //homepage
    await page.goto(url);

    //Costs page
    await page.getByRole('link', { name: 'Costs' }).click();
    await expect(page).toHaveURL(`${url}/costpage`);

    //Clearing leftovers
    page.once('dialog', async dialog => {
        await dialog.accept();
    });
    await trClickButtonIfExists(page, '123.45', 'Delete');
    await trClickButtonIfExists(page, 'Qwerty321', 'Delete');

    //Add cost
    await page.getByRole('link', { name: 'Add New Cost' }).click();
    await expect(page).toHaveURL(`${url}/cost/add`);

    //Partial fill not accepted
    const button = page.getByRole('button', { name: 'save' });
    await page.locator('#eventId').selectOption({ label: 'Joulukonsertti' });
    await button.click()
    await expect(page).toHaveURL(`${url}/cost/add`);
    await page.locator('#typeId').selectOption({ label: 'Aikuinen' });
    await button.click()
    await expect(page).toHaveURL(`${url}/cost/add`);

    //Add works
    await page.getByLabel('price').fill('123.45');
    await button.click()
    await expect(page).toHaveURL(`${url}/costpage`);
    await expect(page.getByText('123.45')).toBeVisible();

    //Edit works
    await page
        .locator('tr', { has: page.getByText('123.45') })
        .getByRole('link', { name: 'Edit' })
        .click();
    await page.getByLabel('price').fill('543.21');
    await page.getByRole('button', { name: 'save' }).click();
    await expect(page.getByText('543.21')).toBeVisible();

    //Delete element
    const del = page
        .locator('tr', { has: page.getByText('543.21') })
        .getByRole('button', { name: 'Delete' })
    await expect(del).toHaveCount(1);

    //Delete works
    await del.click();
    await expect(page.getByText('123.45')).toHaveCount(0);
    await expect(page.getByText('543.21')).toHaveCount(0);

})

//Sale and ticket add, edit delete
test('Sales and tickets CRUD', async () => {
    const url = String(process.env.URL);

    //homepage
    await page.goto(url);

    //Salepage
    await page.getByRole('link', { name: 'Sales' }).click();
    await expect(page).toHaveURL(`${url}/salepage`);

    //Clearing leftovers
    page.once('dialog', async dialog => {
        await dialog.accept();
    });

    const saleRowsStart = await page.locator('tbody tr').count();

    //Add sale page
    await page.getByRole('link', { name: 'Add Sale' }).click();
    await expect(page).toHaveURL(`${url}/sale/add`);

    //Add sale works
    await page.locator('#userId').selectOption({ label: 'admin' });
    await page.getByRole('button', { name: 'save' }).click();
    let saleRowsNew = await page.locator('tbody tr').count();
    expect(saleRowsNew).toBeGreaterThan(saleRowsStart);

    //homepage
    await page.goto(url);

    //Ticketpage
    await page.getByRole('link', { name: 'Tickets', exact: true }).click();
    await expect(page).toHaveURL(`${url}/ticketpage`);
    const ticketRowsStart = await page.locator('tbody tr').count();

    //Add ticket page
    await page.getByRole('link', { name: 'Add Ticket' }).click();
    await expect(page).toHaveURL(`${url}/ticket/add`);

    //Add ticket works
    await page.locator('select[name="selectedEventId"]').selectOption({ label: 'Joulukonsertti' });
    await page.getByRole('button', { name: 'save' }).click();
    let ticketRowsNew = await page.locator('tbody tr').count();
    expect(ticketRowsNew).toBeGreaterThan(ticketRowsStart);

    //Sales page
    await page.goto(`${url}/salepage`)

    //Newest sale has ticket
    const saleRowLast = page.locator('tbody tr').last();
    await expect(saleRowLast).toContainText('Ticket ID:');

    //Delete works
    await saleRowLast.getByRole('button', { name: 'Delete' }).click();
    saleRowsNew = await page.locator('tbody tr').count();
    expect(saleRowsStart).toBe(saleRowsNew);


    //Ticket page
    await page.goto(`${url}/ticketpage`)

    //Ticket has also been deleted
    ticketRowsNew = await page.locator('tbody tr').count();
    expect(ticketRowsStart).toBe(ticketRowsNew);
})