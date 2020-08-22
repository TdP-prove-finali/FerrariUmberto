# Applicazione per la gestione di un servizio di bike sharing - CompassBike

## Istruzioni per l'installazione dell'applicazione

1. Effettuare il fork del repository oppure scaricarlo sotto forma di archivio ZIP.
2. Importare il progetto in Eclipse, tramite l'archivio scaricato oppure copiando la URI del repository creata nel proprio GitHub a seguito del fork.
3. Eseguire lo script `bike_sharing_db.sql` all'interno dell'archivio ZIP omonimo contenuto nella cartella `Database` tramite il proprio DBMS.
4. Inserire la password del DBMS utilizzato nel campo `setPassword` all'interno della classe `DBConnect`.
5. Eseguire la classe `Main` per avviare l'applicazione.

Video dimostrativo sull'uso dell'applicazione disponibile al link: https://youtu.be/wRPsn2E0DYg

## Dataset

I dataset utilizzati a scopo di esempio sono relativi al servizio di bike sharing <a href="https://www.santandercycles.co.uk/">Santander Cycles</a> della città di Londra. I dati sono rilasciati da <a href="https://tfl.gov.uk/">Transport for London</a> con licenza <a href="https://tfl.gov.uk/corporate/terms-and-conditions/transport-data-service">Open Government Licence 2.0</a>.

Nella cartella `Database` sono forniti dei file che possono essere utilizzati per testare le funzionalità di importazione dell'applicazione.

Ulteriori dati da importare possono essere scaricati al seguente link: https://cycling.data.tfl.gov.uk/.

## Librerie

Nell'applicazione sono state utilizzate le seguenti librerie:

-  <a href="https://github.com/stleary/JSON-java">JSON-Java</a> 
-  <a href="https://jgrapht.org/">JGraphT</a> 
-  <a href="http://www.jfoenix.com/">JFoenix</a> 
-  <a href="https://github.com/Jerady/fontawesomefx-glyphsbrowser">FontAwesomeFX</a> 
-  <a href="https://docs.mapbox.com/mapbox-gl-js/api/">MapBox GL</a> libreria `JavaScript` utilizzata per creare le mappe interattive.

---

*Umberto Ferrari*