import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseInternshipsComponent } from './browse-internships.component';

describe('BrowseInternshipsComponent', () => {
  let component: BrowseInternshipsComponent;
  let fixture: ComponentFixture<BrowseInternshipsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrowseInternshipsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BrowseInternshipsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
