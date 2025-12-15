package mycode.pisicaspring.view;

import mycode.pisicaspring.dtos.*;
import mycode.pisicaspring.exceptions.*;
import mycode.pisicaspring.service.PisicaCommandService;
import mycode.pisicaspring.service.PisicaQuerryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class View {
    private final PisicaQuerryService pisicaQuerryService;
    private final PisicaCommandService pisicaCommandService;
    private final Scanner scanner;

    public View(PisicaCommandService pisicaCommandService, PisicaQuerryService pisicaQuerryService) {
        this.pisicaQuerryService = pisicaQuerryService;
        this.pisicaCommandService = pisicaCommandService;
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        System.out.println("--- Meniu Pisica Spring ---");
        System.out.println("1 -> Afiseaza toate pisicile");
        System.out.println("2->Adauga pisica");
        System.out.println("3->Vezi pisicile mai batrane de 5 ani");
        System.out.println("4->Sterge pisica");
        System.out.println("5->Update pisica");
        System.out.println("6->Vezi toate rasele unice");
        System.out.println("7->Cauta pisici dupa varsta");
        System.out.println("8->Vezi pisicile oronate dupa nume");
        System.out.println("9->Vezi pisicile dupa rasa orodnate dupa varsta");
        System.out.println("10->Vezi top 3 cele mai tinere pisici");
        System.out.println("11->Cauta pisici dupa nume (incepe cu...)");
        System.out.println("12->Cauta pisici dupa varsta exacta");
        System.out.println("13->Afiseaza Top 5 cele mai batrane pisici");
    }

    public void play() {
        boolean merge = true;
        while (merge) {
            this.menu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> viewPisica();
                case 2 -> viewAddPisica();
                case 3 -> viewCatsOlderThan();
                case 4 -> viewStergePisica();
                case 5 -> viewUpdatePisica();
                case 6 -> viewRasa();
                case 7 -> viewPisiciByAgeRange();
                case 8 -> viewPisiciOrderedByAge();
                case 9 -> viewPisiciByRasaOrderedByVarsta();
                case 10 -> viewTop3Youngest();
                case 11 -> viewByNumeStartingWith();
                case 12 -> viewByVarstaExact();
                case 13 -> viewTop5Oldest();
            }
        }
    }

    public void viewPisica() {
        Pageable pageable = readPagination(Sort.by("id").ascending());
        PisicaListResponsePageable pagedResponse = pisicaQuerryService.getAllPisici(pageable);
        List<PisicaResponse> pisicaList = pagedResponse.pisicaDtoList();
        if (pisicaList == null || pisicaList.isEmpty()) {
            System.out.println("Nu s-a gasit nicio pisica.");
            return;
        }
        System.out.println("Pagina " + pagedResponse.PageNumber() + " din " + pagedResponse.totalPages());
        pisicaList.forEach(pisica ->
                System.out.println("ID: " + pisica.id() + ", Nume: " + pisica.nume() + ", Rasa: " + pisica.rasa() + ", Varsta: " + pisica.varsta())
        );
    }

    public void viewAddPisica() {
        System.out.print("Nume: ");
        String nume = scanner.nextLine();
        System.out.print("Rasa: ");
        String rasa = scanner.nextLine();
        System.out.print("Varsta: ");
        int varsta = scanner.nextInt();
        try {
            PisicaDto pisicaDto = new PisicaDto(rasa, varsta, nume);
            pisicaCommandService.createPisica(pisicaDto);
            System.out.println("Pisica '" + nume + "' a fost adaugata cu succes!");
        } catch (PisicaAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    public void viewCatsOlderThan() {
        Pageable pageable = readPagination(Sort.by("varsta").ascending());
        List<PisicaNumeVarstaDto> listaPisici = pisicaQuerryService.getOlderPisiciInfo(5, pageable);
        if (listaPisici.isEmpty()) {
            System.out.println("Nu s-au gasit pisici mai batrane de " + 5 + " ani.");
            return;
        }
        System.out.println("Pisici mai batrane de " + 5 + " ani");
        listaPisici.forEach(info -> System.out.println("Nume: " + info.nume() + ", Varsta: " + info.varsta()));
    }

    public void viewStergePisica() {
        System.out.println("Introdu numele pisicii pe care vrei sa o stergi din baza de date");
        String nume = scanner.nextLine();
        PisicaDto pisicaDto = new PisicaDto("", 0, nume);
        try {
            pisicaCommandService.deletePisicaByName(pisicaDto);
            System.out.println("Pisica " + pisicaDto.nume() + " a fost stearsa cu succes");
        } catch (PisicaDoesntExistException e) {
            e.printStackTrace();
        }
    }

    public void viewUpdatePisica() {
        System.out.println("Introduceti numele la pisica pe care vreti sa o updatati");
        String nume = scanner.nextLine();
        System.out.println("Introduceti datele noii pisici");
        System.out.println("Numele nou");
        String numeNou = scanner.nextLine();
        System.out.println("Rasa noua");
        String rasaNoua = scanner.nextLine();
        System.out.println("Varsta noua");
        int varstaNoua = scanner.nextInt();
        PisicaDto newPisica = new PisicaDto(rasaNoua, varstaNoua, numeNou);
        try {
            pisicaCommandService.updatePisicaByName(nume, newPisica);
            System.out.println("Pisica " + nume + " a fost updatata cu succes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewRasa() {
        try {
            Pageable pageable = readPagination(Sort.by("rasa").ascending());
            PisicaRasaListRequest rase = pisicaQuerryService.findAllUniqueRase(pageable);
            rase.pisicaRasaInfoList().forEach(pisicaRasaInfo -> System.out.println("Rasa: " + pisicaRasaInfo.rasa()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Pageable readPagination(Sort sort) {
        System.out.print("Introdu pagina dorita (0 pentru prima pagina): ");
        int page = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Introdu dimensiunea paginii: ");
        int size = scanner.nextInt();
        scanner.nextLine();
        return PageRequest.of(page, size, sort);
    }

    public void viewPisiciByAgeRange() {
        System.out.println("Cauta Pisici Dupa Varsta");
        System.out.print("Varsta Minima: ");
        int varstaMin = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Varsta Maxima: ");
        int varstaMax = scanner.nextInt();
        scanner.nextLine();
        Pageable pageable = readPagination(Sort.by("varsta").ascending());
        PisicaVarstaRangeListRequest listWrapper = pisicaQuerryService.findPisiciByVarstaRange(varstaMin, varstaMax, pageable);
        List<PisicaVarstaRangeInfo> pisici = listWrapper.pisiciList();
        if (pisici.isEmpty()) {
            System.out.println("Nu s-au gasit pisici cu varsta intre " + varstaMin + " si " + varstaMax + ".");
            return;
        }
        System.out.println("Pisici Gasite");
        pisici.forEach(info -> System.out.println("Nume: " + info.nume() + ", Rasa: " + info.rasa() + ", Varsta: " + info.varsta()));
    }

    public void viewPisiciOrderedByAge() {
        System.out.println("Pisicile ordonate dupa varsta: ");
        Pageable pageable = readPagination(Sort.by("varsta").ascending());
        PisicaListRequest pisicaListRequest = pisicaQuerryService.getAllByPisiciOrderedByVarsta(pageable);
        pisicaListRequest.pisicaDtoList().forEach(pisica ->
                System.out.println("Nume: " + pisica.nume() + " rasa: " + pisica.rasa() + " varsta" + pisica.varsta()));
    }

    public void viewPisiciByRasaOrderedByVarsta() {
        System.out.println("Introduceti rasa");
        String rasa = scanner.nextLine();
        try {
            Pageable pageable = readPagination(Sort.by("varsta").ascending());
            PisicaNumeVarstaListRequest listRequest = pisicaQuerryService.findByRasaSortedByVarsta(rasa, pageable);
            listRequest.pisiciList().forEach(info -> System.out.println("Nume: " + info.nume() + " Varsta " + info.varsta()));
        } catch (RasaNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void viewTop3Youngest() {
        System.out.println("Top 3 Cele Mai Tinere Pisici");
        try {
            PisicaListRequest listWrapper = pisicaQuerryService.findTop3YoungestPisici();
            listWrapper.pisicaDtoList().forEach(info ->
                    System.out.println("Nume: " + info.nume() + ", Rasa: " + info.rasa() + ", Varsta: " + info.varsta()));
        } catch (PisicaDoesntExistException e) {
            e.printStackTrace();
        }
    }

    public void viewByNumeStartingWith() {
        System.out.print("Introduce numele care incepe cu: ");
        String numeStart = scanner.nextLine();
        try {
            Pageable pageable = readPagination(Sort.by("nume").ascending());
            PisicaNumeRasaListRequest listWrapper = pisicaQuerryService.findByNumeStartingWith(numeStart, pageable);
            listWrapper.pisiciList().forEach(info -> System.out.println("Nume: " + info.nume() + ", Rasa: " + info.rasa()));
        } catch (NoPisicaFoundException e) {
            e.printStackTrace();
        }
    }

    public void viewByVarstaExact() {
        System.out.print("Introduce varsta exacta: ");
        int varsta = scanner.nextInt();
        scanner.nextLine();
        try {
            Pageable pageable = readPagination(Sort.by("nume").ascending());
            PisicaIdNumeRasaListRequest listWrapper = pisicaQuerryService.findByVarstaExact(varsta, pageable);
            listWrapper.pisiciList().forEach(info ->
                    System.out.println("ID: " + info.id() + ", Nume: " + info.nume() + ", Rasa: " + info.rasa()));
        } catch (NoPisicaFoundException e) {
            e.printStackTrace();
        }
    }

    public void viewTop5Oldest() {
        System.out.println("Top 5 Cele Mai Batrane Pisici");
        try {
            PisicaListRequest listWrapper = pisicaQuerryService.findTop5OldestPisici();
            listWrapper.pisicaDtoList().forEach(info ->
                    System.out.println("Nume: " + info.nume() + ", Rasa: " + info.rasa() + ", Varsta: " + info.varsta()));
        } catch (NoResultsTopQueryException e) {
            e.printStackTrace();
        }
    }

    public void viewAverageAgeByRasa() {
        System.out.println("Varsta Medie pe Rasa");
        try {
            Pageable pageable = readPagination(Sort.by("rasa").ascending());
            RasaAverageAgeListRequest listWrapper = pisicaQuerryService.findAverageAgeByRasa(pageable);
            listWrapper.averageAgeList().forEach(info ->
                    System.out.println("Rasa: " + info.rasa() + " Varsta Medie: " + info.averageAge()));
        } catch (NoResultsTopQueryException e) {
            e.printStackTrace();
        }
    }
}
