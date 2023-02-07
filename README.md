# Regex Generator

## Traccia

Scrivere un algoritmo in Java o Kotlin che dato un elenco di codici alfanumerici restituisce
la regular expression che meglio "riassume" l'elenco fornito.

Esempio 1  
Dato un elenco di targhe italiane:  
AB123ZZ  
BB742TG  
CF678HG  
...  
L'algoritmo deve restituire la regexp per indicare 2 lettere, 3 cifre, 2 numeri:  
[A-Z]{2}\d{3}[A-Z]{2}

Esempio 2:  
Dato un elenco di codici fiscali:  
TNTTST80A01F205E  
...  
Il risultato deve essere la regexp per 6 lettere, 2 cifre, 1 lettera, 2 cifre, 1 lettera, 3 cifre, 1 lettera:  
[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]

Esempio 3:  
Dati i codici:  
AA123  
BA1234  
AB12345  
Il risultato deve essere la regexp per 2 lettere seguite da un numero variabile da 3 a 5 cifre:  
[A-Z]{2}\d{3,5}

Esempio 4:  
A123XY  
BA1234ZT  
AB12345B  
Il risultato deve essere   
[A-Z]{1,2}\d{3,5}[A-Z]{1,2}

L'algoritmo non deve avere alcuna conoscenza a priori sul formato dei codici in input per cui
deve basarsi su quanche forma di analisi dell'input.

Ulteriori dati sui codici di input:
- sono composti da sole lettere [A-Z] e cifre \d
- hanno effettivamente una regolare alternanza di lettere/cifre, cioè non sono stringhe
  casuali o irregolari
- iniziano sempre con una lettera

Il problema permette una certa flessibilità di interpretazione. Se serve, completare
spiegando le ipotesi e le scelte fatte.


--- 
## Risoluzione

### Introduzione

Non avendo alcuna conoscenza a priori sul formato dei codici in input ci si aspetta che l'algoritmo  visiti almeno una volta ogni stringa nella lista, cosi come ogni carattere delle stringhe per poterne valutare la composizione. Ne consegue una complessità non minore di *O(nm)* dove n è la lunghezza della lista di stringhe e m è la lunghezza della stringa più lunga.

Le informazioni contenute in *Ulteriori dati sui codici di input* sono state utilizzate per implementare i seguenti controlli sugli input;
- Le stringhe in input devono contenere solo lettere *[A-Z]* e cifre *\d*
- Le stringhe possono variare nel numero di caratteri ma devono rispettare la stessa sequenza di lettere e numeri
- Le stringhe devono iniziare con un carattere *[A-Z]*

Per completezza sono stati aggiunti i seguenti controlli:
- La lista in input non deve essere *null* o *empty*
- Ogni input nella lista non deve essere *null* o *blank*

I 4 esempi proposti sono stati inclusi negli Unit Test

### Implementazione

L'algoritmo è stato implementato nella classe *AlphaNumericRegexGenerator*

Può essere logicamente suddiviso in tre fasi;
1. Costruzione della base della regex - viene definita la sequenza effettiva dei token in base alla prima stringa.   
   Nell'implementazione avviene con la chiamata al metodo *createRegexToken()*.


2. Aggiornamento dei range dei token in base ai caratteri individuati nelle stringhe successive alla prima.  
   Nell’implementazione avviene con la chiamata al metodo *updateRegexToken()*.


3. Composizione dell'output  - generati dei token stringa in base a minimi/massimi e concatenazione nella regex da mandare in output.   
   Nell’implementazione avviene con la chiamata al metodo *computeRegexTokenList()*.

A supporto della costruzione della regex è stata creata la classe *RegexToken*. Ogni istanza di 
RegexToken rappresenta un token che individua una specifica substring. E' caratterizzato da TokenType per indicare se il token individua lettere o numeri ed un minimo e massimo, che indicano il range del token.

La classe RegexToken inoltre supporta la generazione del token stringa tramite il metodo *compute()* secondo i seguenti pattern:
- match di un singolo carattere - e.g. [A-Z]
- match di un numero esatto di caratteri maggiore di uno  - e.g. [A-Z]{2}
- match di un range di caratteri di caratteri - e.g. [A-Z]{1,3}


### Esempio Esecuzione

Test Case 4 - Input List: ["A123XY","BA1234ZT","AB12345B"]

1. Creazione sequenza base + Match prima stringa  
   Input: A123XY   
   [#RegexTokenA{tokenType=LETTER, min=1, max=1},  
   #RegexTokenB{tokenType=DIGIT, min=3, max=3},  
   #RegexTokenC{tokenType=LETTER, min=2, max=2}]


2. Update per match seconda stringa:  
   Input: BA1234ZT   
   [#RegexTokenA{tokenType=LETTER, min=1, max=2},  
   #RegexTokenB{tokenType=DIGIT, min=3, max=4},  
   #RegexTokenC{tokenType=LETTER, min=2, max=2}]


3. Update per match terza stringa :  
   Input: AB12345B  
   [#RegexTokenA{tokenType=LETTER, min=1, max=2},  
   #RegexTokenB{tokenType=DIGIT, min=3, max=5},  
   #RegexTokenC{tokenType=LETTER, min=1, max=2}]


4. Output parziale:  
   [#RegexTokenA{tokenType=LETTER, min=1, max=2} = [A-Z]{1,2}  
   #RegexTokenB{tokenType=DIGIT, min=3, max=5} = \d{3,5}  
   #RegexTokenC{tokenType=LETTER, min=1, max=2}] = [A-Z]{1,2}  


5. Output finale:  
   [A-Z]{1,2}\d{3,5}[A-Z]{1,2} 