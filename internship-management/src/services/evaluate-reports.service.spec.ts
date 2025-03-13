import { TestBed } from '@angular/core/testing';

import { EvaluateReportsService } from './evaluate-reports.service';

describe('EvaluateReportsService', () => {
  let service: EvaluateReportsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvaluateReportsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
