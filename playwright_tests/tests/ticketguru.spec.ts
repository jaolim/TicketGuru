import { test, expect } from '@playwright/test';
import * as dotenv from 'dotenv';

dotenv.config();

// Page is responsive
test('TicketGuru responds', async ({ page }) => {
    const url = String(process.env.URL);

    await page.goto(url);

    await expect(page.getByText("pääsylippusi")).toBeVisible();
})
