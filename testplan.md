# Testausprosessit -dokumentaatio

Tässä dokumentissa käydään läpi projektin testausprosessit. Testauksen tavoitteena on varmistaa, että ohjelmsito toimii suunnitellusti. Eli virheet havaitaan ja järjestelmäne eri komponentit toimivat yhdessä. Testaus pitää sisällään yksikkötestejä, integraatiotestejä sekä end-2-end-testejä. 

1. [Yksikkötestit](#yksikkötestit)
2. [Integraatiotestit](#integraatiotestit)
3. [End-2-end-testit](#end-to-end-testit)

## Yksikkötestit
Yksikkötestien tarkoituksena on varmistaa että yksittäiset koodikomponentit toimivat oikein. Testataan luokan tai metodin toimintaa erillään muusta järjestelmästä. 

### Cost-luokka

Testi `shouldCreateCostWithCorrectValues()` tarkistaa, että `Cost`-oliolle on annettu hinta, `TicketType` ja `Event` tallentuvat oikein.
Testi `shouldUpdatePrice()` varmistaa, että `setPrice()`muuttaa hinnan oikein.
Testi `shouldThrowExceptionForNegativePrice()`tarkistaa, että negatiivinen hinta aiheuttaa poikkeuksen.


## Integraatiotestit
Sama periaate kuin yksikkötesteissä, mutta testaa useamman komponentin yhteistyötä. `@SpringBootTest`lataa koko sovelluskontekstin, jonka avulla testattavat objektit kommunikoivat tietokannan kanssa ja sovelluksen eri osien välinen logiikka testautuu.

### Cost-luokan CRUD-toiminnot
`shouldCreateNewCost()` testaa, että uusi hinta tallentuu tietokantaan.
`shouldReturnCostById()` testaa, että tietokannasta haetaan oikea hinta ID:n perusteella.
`shouldDeleteCost()` testaa, että hinnan voi poistaa ja sitä ei löydä enää tietokannasta.
`shouldUpdatePrice()` testaa, että hinnan päivitys onnsituu ja tallentuu tietokantaan. 


## End-2-end-testit