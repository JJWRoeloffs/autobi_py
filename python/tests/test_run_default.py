from autobi.core import AutobiJVMHandler
from autobi import RunDefault


class TestRunDefault:
    def test_runs_at_all(self):
        with AutobiJVMHandler("test") as jvm:
            RunDefault(jvm).run("")
            # "-input_file=test_data/interviened.TextGrid -wav_file=test_data/test.wav -out_file=test_data/out.TextGrid -arff_file=out.arff -pitch_accent_detector=AuToDI/bdc_burnc.acc.detection.model -pitch_accent_classifier=AuToDI/bdc_burnc.acc.classification.model -intonational_phrase_boundary_detector=AuToDI/bdc_burnc.intonp.detection.model -intermediate_phrase_boundary_detector=AuToDI/bdc_burnc.interp.detection.model -boundary_tone_classifier=AuToDI/bdc_burnc.pabt.classification.model -phrase_accent_classifier=AuToDI/bdc_burnc.phacc.classification.model"
