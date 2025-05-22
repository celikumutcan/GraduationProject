import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferApplicantsComponent } from './offer-applicants.component';

describe('OfferApplicantsComponent', () => {
  let component: OfferApplicantsComponent;
  let fixture: ComponentFixture<OfferApplicantsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OfferApplicantsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OfferApplicantsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
