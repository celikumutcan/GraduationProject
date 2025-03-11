import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Router} from '@angular/router';
import {
  InitialTraineeInformationForm,
  TraineeInformationFormService
} from '../../../services/trainee-information-form.service';
import {UserService} from '../../../services/user.service';



@Component({
  selector: 'app-check-forms',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './check-forms.component.html',
  styleUrls: ['./check-forms.component.css'],
})
export class CheckFormsComponent implements OnInit {
  forms: FormData[] = [];
  isFormVisible = false;
  isEditing = false;

  initialForms: any[] = [];
  approvedForms: any[] = [];
  sortedForms: any[] = []; // To store all forms sorted by datetime

  userName = '';
  userFirstName ='';
  userLastName = '';

  isAddingNewCompany = false;
  isAddingNewBranch = false;

  formData =  {
  type: '',
  code: '',
  semester: '',
  health_insurance: false,
  fill_user_name: '',
  company_user_name: '',
  branch_name: '',
  company_branch_country: '',
  company_branch_city: '',
  company_branch_district: '',
  company_branch_address: '',
  company_branch_phone: '',
  company_branch_email: '',
  position: '',
  startDate: '',
  endDate: '',
};

  companies = [""];
  branches = [""];
  countries: string[] = [];


  constructor(
    private userService: UserService,
    private traineeInformationFormService: TraineeInformationFormService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userName = this.userService.getUser().userName;
    this.userFirstName = this.userService.getUser().firstName;
    this.userLastName = this.userService.getUser().lastName;
    this.fetchCompanies();
    this.fetchStudentTraineeInformationForms();

  }
  fetchStudentTraineeInformationForms(): void {
    this.traineeInformationFormService.getStudentTraineeForms(this.userName).subscribe({
      next: (data: [any[], any[]]) => {
        this.initialForms = data[0];
        this.approvedForms = data[1];
        this.combineAndSortForms();
      },
      error: (err) => {
        console.error('Error fetching Trainee Information Forms', err);
      }
    });
  }

  fetchCompanies(): void {
    this.traineeInformationFormService.getCompanies().subscribe({
      next: (data: { fname: string }[]) => {
        this.companies = data.map(company => company.fname);
      },
      error: (err) => {
        console.error('Error fetching Trainee Information Forms', err);
      }
    });
  }

  fetchCompanyBranches(companyName: string): void {
    if (companyName) {
      this.traineeInformationFormService.getCompanyBranches(companyName).subscribe({
        next: (data: { branch_name: string }[]) => {
          this.branches = data.map(branch => branch.branch_name);
        },
        error: (err) => {
          console.error('Error fetching branches', err);
        }
      });
    }
  }

  combineAndSortForms(): void {
    const combinedForms = [...this.initialForms, ...this.approvedForms];
    this.sortedForms = combinedForms.sort(
      (a, b) => new Date(b.datetime).getTime() - new Date(a.datetime).getTime()
    );
  }

  showDetails(form: any): void {
    this.router.navigate(['/internship-details'], { state: { internship: form } });
  }



  closeModal() {
    this.isFormVisible = false;
    this.resetForm();
    this.isEditing=false;

  }

  onCompanyChange(event: any) {
    const selectedCompany = (event.target as HTMLSelectElement).value;
    if(!this.isAddingNewCompany){
      this.formData.company_user_name = selectedCompany;
      if (selectedCompany) {
        this.fetchCompanyBranches(selectedCompany);
      }
    }
  }

  onBranchChange(event: any) {
    const selectedBranch = (event.target as HTMLSelectElement).value;
    if(!this.isAddingNewBranch){
      this.formData.branch_name = selectedBranch;
    }
  }

  toggleNewCompany(event: any) {
    const checked = event.target.checked;
    this.isAddingNewCompany= checked;
    this.isAddingNewBranch = checked;

  }

  toggleNewBranch(event: any) {
    const checked = event.target.checked;
    this.isAddingNewBranch = checked;

    this.countries= [
      "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda",
      "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
      "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia",
      "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso",
      "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Central African Republic",
      "Chad", "Chile", "China", "Colombia", "Comoros", "Congo (Congo-Brazzaville)", "Costa Rica",
      "Croatia", "Cuba", "Cyprus", "Czechia", "Denmark", "Djibouti", "Dominica",
      "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
      "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France", "Gabon",
      "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea",
      "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia",
      "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan",
      "Kenya", "Kiribati", "Korea, North", "Korea, South", "Kosovo", "Kuwait", "Kyrgyzstan",
      "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania",
      "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
      "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro",
      "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands",
      "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Macedonia", "Norway", "Oman", "Pakistan",
      "Palau", "Palestine State", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines",
      "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
      "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia",
      "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
      "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden",
      "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo",
      "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine",
      "United Arab Emirates", "United Kingdom", "United States of America", "Uruguay", "Uzbekistan",
      "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
    ];
    console.log(this.countries);
  }

  saveForm() {
    console.log(this.formData);
    const newForm: InitialTraineeInformationForm = {
      type: this.formData.type,
      code: this.formData.code,
      semester: '2025 Fall',
      health_insurance: this.formData.health_insurance == true,
      fill_user_name: this.userName,
      company_user_name: this.formData.company_user_name,
      branch_name: this.formData.branch_name ,
      company_branch_address:  (this.isAddingNewBranch )
        ? this.formData.company_branch_address
        : '',
      company_branch_phone: (this.isAddingNewBranch )
        ? this.formData.company_branch_phone
        : '',
      company_branch_email: (this.isAddingNewBranch )
        ? this.formData.company_branch_email
        : '',
      company_branch_city: (this.isAddingNewBranch )
        ? this.formData.company_branch_city
        : '',
      company_branch_country: (this.isAddingNewBranch )
        ? this.formData.company_branch_country
        : '',
      company_branch_district: (this.isAddingNewBranch )
        ? this.formData.company_branch_district
        : '',
      position: this.formData.position,
      startDate: this.formData.startDate,
      endDate: this.formData.endDate
    };
    console.log("message:",newForm,this.formData,this.isAddingNewBranch,this.isAddingNewCompany,this.formData.branch_name);
    // Call the service method
    this.traineeInformationFormService
      .addNewStudentTraineeInformationForm(newForm)
      .subscribe({
        next: (response: any) => {
          if (response && response.status === 201) {
            console.log('Form submitted successfully', response);
            this.closeModal();
            this.resetForm();
            this.fetchStudentTraineeInformationForms();
          } else {
            console.warn('Unexpected response', response);
          }
        },
        error: (err) => {
          console.error('Error submitting the form', err);

          this.closeModal();
          this.resetForm();
          this.fetchStudentTraineeInformationForms();
        }
      });
  }

  resetForm() {
    this.formData =  {
      type: '',
      code: '',
      semester: '',
      health_insurance: false,
      fill_user_name: '',
      company_user_name: '',
      branch_name: '',
      company_branch_country: '',
      company_branch_city: '',
      company_branch_district: '',
      company_branch_address: '',
      company_branch_phone: '',
      company_branch_email: '',
      position: '',
      startDate: '',
      endDate: '',
    };
    this.isAddingNewCompany = false;
    this.isAddingNewBranch = false;
    this.branches = [];
  }

  /*
  initializeFormIdCounter(): void {
    // Find the highest ID in the existing forms and set the counter accordingly
    if (this.forms.length > 0) {
      const maxId = Math.max(...this.forms.map(form => parseInt(form.id, 10)));
      this.formIdCounter = maxId + 1;
    }
  }*/

  loadForms(): void {
    const storedForms = localStorage.getItem("traineeForms");
    try {
      this.forms = storedForms ? JSON.parse(storedForms) : [];
    } catch {
      this.forms = [];
    }
  }



  refresh(){
    const currentUrl = this.router.url;
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigateByUrl(currentUrl);
    });
  }

  editForm(index: number): void {
    this.isEditing = true;

    this.isFormVisible = true;
  }

  deleteForm(index: number): void {
    if (confirm('Are you sure you want to delete this form?')) {
      this.forms.splice(index, 1); // Remove the form from the array
      this.saveForms(); // Update localStorage
      alert('Form deleted successfully.');
    }
  }

  openForm(): void {
    this.isFormVisible = true;
  }

  closeForm(): void {
    this.isFormVisible = false;
    this.resetForm();
  }



  saveForms(): void {
    localStorage.setItem("traineeForms", JSON.stringify(this.forms));
  }

  clearAllForms(): void {
    if (confirm('Are you sure you want to delete all forms? This action cannot be undone.')) {
      localStorage.removeItem('traineeForms'); // Clear localStorage
      this.forms = []; // Reset the forms array
      alert('All forms have been deleted.');
    }
  }
}
