import { Page } from '@playwright/test';

export async function trClickLinkIfExists(page: Page, row: string, element: string) {
    const clickable = page
        .locator('tr', { hasText: row })
        .getByRole('link', { name: element })

    if (await clickable.count()) {
        await clickable.click();
    }
}

export async function trClickButtonIfExists(page: Page, row: string, element: string) {
    const clickable = page
        .locator('tr', { hasText: row })
        .getByRole('button', { name: element })

    if (await clickable.count()) {
        await clickable.click();
    }
}