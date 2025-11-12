package mycode.pisicaspring.view;

import mycode.pisicaspring.dtos.PisicaDto;
import mycode.pisicaspring.dtos.PisicaListRequest;
import mycode.pisicaspring.exceptions.PisicaAlreadyExistsException;
import mycode.pisicaspring.service.PisicaCommandService;
import mycode.pisicaspring.service.PisicaQuerryService;
import org.springframework.stereotype.Component;

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
    }


    public void play() {
        boolean merge = true;
        while (merge) {
            this.menu();
            int choice =scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    viewPisica();
                    break;
                case 2:
                    viewAddPisica();
                    break;
            }
        }
    }

    public void viewPisica(){
        PisicaListRequest pisicaListRequest = pisicaQuerryService.getAllPisici();
        if (pisicaListRequest == null || pisicaListRequest.pisicaDtoList() == null || pisicaListRequest.pisicaDtoList().isEmpty()) {
            System.out.println("Nu s-a gasit nicio pisica.");
            return;
        }
        pisicaListRequest.pisicaDtoList().forEach(pisica -> {
            System.out.println("Nume: "+pisica.nume() +", Rasa: " + pisica.rasa() +",Varsta: "+ pisica.varsta());
        });
    }
    public void viewAddPisica(){
        System.out.print("Nume: ");
        String nume = scanner.nextLine();
        System.out.print("Rasa: ");
        String rasa = scanner.nextLine();
        System.out.print("Varsta: ");
        int varsta=scanner.nextInt();
        try{
            PisicaDto pisicaDto=new PisicaDto(rasa,varsta,nume);

            pisicaCommandService.createPisica(pisicaDto);
            System.out.println("Pisica '" + nume + "' a fost adaugata cu succes!");

        }catch (PisicaAlreadyExistsException e){
            e.printStackTrace();
        }






    }

    }
