import { TestBed } from '@angular/core/testing';

import { TraineeInformationFormService } from './trainee-information-form.service';

describe('TraineeInformationFormService', () => {
  let service: TraineeInformationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TraineeInformationFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
